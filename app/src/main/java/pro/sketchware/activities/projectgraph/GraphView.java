package pro.sketchware.activities.projectgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.EnumSet;
import java.util.Set;

/**
 * Кастомный View для отрисовки графа проекта.
 * Поддерживает зум (pinch), панорамирование (drag), тап по узлу.
 */
public class GraphView extends View {

    // Размеры узлов
    private static final float NODE_WIDTH = 220f;
    private static final float NODE_HEIGHT = 70f;
    private static final float NODE_RADIUS = 12f;
    private static final float LEVEL_GAP_X = 280f;
    private static final float LEVEL_GAP_Y = 110f;
    private static final float TEXT_PADDING = 12f;
    private static final float TEXT_SIZE = 26f;

    // Цвета по типам
    private static final int COLOR_PROJECT = 0xFF673AB7;   // фиолетовый
    private static final int COLOR_ACTIVITY = 0xFF2196F3;  // синий
    private static final int COLOR_EVENT = 0xFFFF9800;     // оранжевый
    private static final int COLOR_BLOCK = 0xFF4CAF50;     // зелёный
    private static final int COLOR_FUNCTION = 0xFFE91E63;  // розовый
    private static final int COLOR_VARIABLE = 0xFF00BCD4;  // голубой
    private static final int COLOR_COMPONENT = 0xFF795548; // коричневый

    // Paint'ы
    private final Paint nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint edgeLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF tmpRect = new RectF();

    // Данные
    private ProjectGraph graph;
    private final Set<ProjectGraphNode.Type> visibleTypes = EnumSet.allOf(ProjectGraphNode.Type.class);

    // Трансформации
    private float scale = 1f;
    private float translateX = 0f;
    private float translateY = 0f;
    private float focusX = 0f;
    private float focusY = 0f;

    // Жесты
    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;

    // Обработчик тапа
    public interface OnNodeTapListener {
        void onNodeTap(ProjectGraphNode node);
    }

    private OnNodeTapListener tapListener;

    // Drag узлов
    private boolean dragEnabled = true;
    private ProjectGraphNode draggingNode = null;
    private float dragOffsetX = 0f;
    private float dragOffsetY = 0f;

    public GraphView(Context context) {
        this(context, null);
    }

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(0xFF121212);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setFakeBoldText(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(3f);
        borderPaint.setColor(0xFFFFFFFF);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setStrokeWidth(4f);
        edgePaint.setColor(0x99FFFFFF);
        edgeLabelPaint.setColor(0xCCCCCCCC);
        edgeLabelPaint.setTextSize(20f);
        edgeLabelPaint.setTextAlign(Paint.Align.CENTER);

        scaleDetector = new ScaleGestureDetector(getContext(),
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    @Override
                    public boolean onScale(@NonNull ScaleGestureDetector detector) {
                        float factor = detector.getScaleFactor();
                        scale *= factor;
                        scale = Math.max(0.3f, Math.min(3f, scale));
                        invalidate();
                        return true;
                    }
                });

        gestureDetector = new GestureDetector(getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(@NonNull MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2,
                                            float distanceX, float distanceY) {
                        translateX -= distanceX;
                        translateY -= distanceY;
                        invalidate();
                        return true;
                    }

                    @Override
                    public boolean onSingleTapUp(@NonNull MotionEvent e) {
                        handleTap(e.getX(), e.getY());
                        return true;
                    }
                });
    }

    public void setGraph(ProjectGraph graph) {
        this.graph = graph;
        if (graph != null) {
            layout();
            // Авто-центрирование
            post(() -> {
                translateX = (getWidth() - computeGraphWidth() * scale) / 2f;
                translateY = 60f;
                invalidate();
            });
        }
        invalidate();
    }

    public void setOnNodeTapListener(OnNodeTapListener l) {
        this.tapListener = l;
    }

    public void setDragEnabled(boolean enabled) {
        this.dragEnabled = enabled;
        if (!enabled) draggingNode = null;
    }

    public boolean isDragEnabled() {
        return dragEnabled;
    }

    /**
     * Рендер графа в Bitmap.
     * @param fullTree true — всё дерево целиком, false — только видимая область.
     */
    public android.graphics.Bitmap renderToBitmap(boolean fullTree) {
        if (graph == null || graph.nodes.isEmpty()) {
            return android.graphics.Bitmap.createBitmap(10, 10,
                    android.graphics.Bitmap.Config.ARGB_8888);
        }

        if (!fullTree) {
            // Только видимое
            android.graphics.Bitmap bmp = android.graphics.Bitmap.createBitmap(
                    Math.max(1, getWidth()), Math.max(1, getHeight()),
                    android.graphics.Bitmap.Config.ARGB_8888);
            android.graphics.Canvas canvas = new android.graphics.Canvas(bmp);
            draw(canvas);
            return bmp;
        }

        // Всё дерево целиком — считаем bounding box
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE;
        for (ProjectGraphNode n : graph.nodes) {
            if (!visibleTypes.contains(n.type)) continue;
            if (n.x < minX) minX = n.x;
            if (n.y < minY) minY = n.y;
            if (n.x + NODE_WIDTH > maxX) maxX = n.x + NODE_WIDTH;
            if (n.y + NODE_HEIGHT > maxY) maxY = n.y + NODE_HEIGHT;
        }
        if (minX == Float.MAX_VALUE) {
            return android.graphics.Bitmap.createBitmap(10, 10,
                    android.graphics.Bitmap.Config.ARGB_8888);
        }

        float padding = 40f;
        int width = (int) (maxX - minX + padding * 2);
        int height = (int) (maxY - minY + padding * 2);
        if (width < 1) width = 1;
        if (height < 1) height = 1;

        android.graphics.Bitmap bmp = android.graphics.Bitmap.createBitmap(
                width, height, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bmp);
        canvas.drawColor(0xFF121212);

        // Сохраняем текущую трансформацию
        float savedScale = scale;
        float savedTx = translateX;
        float savedTy = translateY;

        try {
            // Устанавливаем так, чтобы весь граф влез
            scale = 1f;
            translateX = -minX + padding;
            translateY = -minY + padding;
            drawGraphContent(canvas);
        } finally {
            scale = savedScale;
            translateX = savedTx;
            translateY = savedTy;
        }

        return bmp;
    }

    /** Отрисовка содержимого графа (без super.onDraw и без save/restore). */
    private void drawGraphContent(android.graphics.Canvas canvas) {
        canvas.save();
        canvas.translate(translateX, translateY);
        canvas.scale(scale, scale);

        // Рёбра
        for (ProjectGraph.Edge edge : graph.edges) {
            if (!isEdgeVisible(edge)) continue;
            ProjectGraphNode from = graph.findNode(edge.fromId);
            ProjectGraphNode to = graph.findNode(edge.toId);
            if (from == null || to == null) continue;
            if (!visibleTypes.contains(from.type) || !visibleTypes.contains(to.type)) continue;

            float x1 = from.x + NODE_WIDTH / 2f;
            float y1 = from.y + NODE_HEIGHT;
            float x2 = to.x + NODE_WIDTH / 2f;
            float y2 = to.y;

            canvas.drawLine(x1, y1, x2, y2, edgePaint);
            drawArrow(canvas, x1, y1, x2, y2);
        }

        // Узлы
        for (ProjectGraphNode node : graph.nodes) {
            if (!visibleTypes.contains(node.type)) continue;
            drawNode(canvas, node);
        }

        canvas.restore();
    }

    public Set<ProjectGraphNode.Type> getVisibleTypes() {
        return visibleTypes;
    }

    public void setVisibleTypes(Set<ProjectGraphNode.Type> types) {
        visibleTypes.clear();
        if (types != null) visibleTypes.addAll(types);
        invalidate();
    }

    public void toggleType(ProjectGraphNode.Type type) {
        if (visibleTypes.contains(type)) {
            visibleTypes.remove(type);
        } else {
            visibleTypes.add(type);
        }
        invalidate();
    }

    public void resetZoom() {
        scale = 1f;
        translateX = (getWidth() - computeGraphWidth()) / 2f;
        translateY = 60f;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        // Drag узла
        if (draggingNode != null) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float x = (event.getX() - translateX) / scale - dragOffsetX;
                float y = (event.getY() - translateY) / scale - dragOffsetY;
                draggingNode.x = x;
                draggingNode.y = y;
                invalidate();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                draggingNode = null;
                return true;
            }
        }

        // Long press — начать drag узла
        if (dragEnabled && event.getAction() == MotionEvent.ACTION_DOWN) {
            ProjectGraphNode node = findNodeAt(event.getX(), event.getY());
            if (node != null) {
                // Запускаем таймер long press
                final float dx = event.getX();
                final float dy = event.getY();
                final ProjectGraphNode target = node;
                postDelayed(() -> {
                    if (target != null) {
                        draggingNode = target;
                        dragOffsetX = (dx - translateX) / scale - target.x;
                        dragOffsetY = (dy - translateY) / scale - target.y;
                    }
                }, 350L);
            }
        }

        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    /** Поиск узла в точке экрана. */
    private ProjectGraphNode findNodeAt(float screenX, float screenY) {
        if (graph == null) return null;
        float x = (screenX - translateX) / scale;
        float y = (screenY - translateY) / scale;
        for (ProjectGraphNode n : graph.nodes) {
            if (!visibleTypes.contains(n.type)) continue;
            if (x >= n.x && x <= n.x + NODE_WIDTH
                    && y >= n.y && y <= n.y + NODE_HEIGHT) {
                return n;
            }
        }
        return null;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (graph == null) return;

        canvas.save();
        canvas.translate(translateX, translateY);
        canvas.scale(scale, scale);

        // Рёбра
        for (ProjectGraph.Edge edge : graph.edges) {
            if (!isEdgeVisible(edge)) continue;
            ProjectGraphNode from = graph.findNode(edge.fromId);
            ProjectGraphNode to = graph.findNode(edge.toId);
            if (from == null || to == null) continue;
            if (!visibleTypes.contains(from.type) || !visibleTypes.contains(to.type)) continue;

            float x1 = from.x + NODE_WIDTH / 2f;
            float y1 = from.y + NODE_HEIGHT;
            float x2 = to.x + NODE_WIDTH / 2f;
            float y2 = to.y;

            canvas.drawLine(x1, y1, x2, y2, edgePaint);
            // Стрелка (маленький треугольник)
            drawArrow(canvas, x1, y1, x2, y2);
        }

        // Узлы
        for (ProjectGraphNode node : graph.nodes) {
            if (!visibleTypes.contains(node.type)) continue;
            drawNode(canvas, node);
        }

        canvas.restore();
    }

    private boolean isEdgeVisible(ProjectGraph.Edge edge) {
        return true; // пока все видны, фильтры по типам узлов работают
    }

    private void drawNode(Canvas canvas, ProjectGraphNode node) {
        tmpRect.set(node.x, node.y, node.x + NODE_WIDTH, node.y + NODE_HEIGHT);
        nodePaint.setColor(getColorForType(node.type));
        canvas.drawRoundRect(tmpRect, NODE_RADIUS, NODE_RADIUS, nodePaint);
        canvas.drawRoundRect(tmpRect, NODE_RADIUS, NODE_RADIUS, borderPaint);

        // Текст (обрезка до одной строки)
        String label = node.label;
        float maxTextWidth = NODE_WIDTH - TEXT_PADDING * 2;
        if (textPaint.measureText(label) > maxTextWidth) {
            while (label.length() > 3 && textPaint.measureText(label + "...") > maxTextWidth) {
                label = label.substring(0, label.length() - 1);
            }
            label = label + "...";
        }
        float textY = node.y + NODE_HEIGHT / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;
        canvas.drawText(label, node.x + TEXT_PADDING, textY, textPaint);
    }

    private void drawArrow(Canvas canvas, float x1, float y1, float x2, float y2) {
        // Направление
        float angle = (float) Math.atan2(y2 - y1, x2 - x1);
        float arrowLength = 18f;
        float arrowAngle = (float) Math.toRadians(25);

        float xA = x2 - arrowLength * (float) Math.cos(angle - arrowAngle);
        float yA = y2 - arrowLength * (float) Math.sin(angle - arrowAngle);
        float xB = x2 - arrowLength * (float) Math.cos(angle + arrowAngle);
        float yB = y2 - arrowLength * (float) Math.sin(angle + arrowAngle);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(edgePaint.getColor());
        p.setStyle(Paint.Style.FILL);
        android.graphics.Path path = new android.graphics.Path();
        path.moveTo(x2, y2);
        path.lineTo(xA, yA);
        path.lineTo(xB, yB);
        path.close();
        canvas.drawPath(path, p);
    }

    private int getColorForType(ProjectGraphNode.Type type) {
        switch (type) {
            case PROJECT: return COLOR_PROJECT;
            case ACTIVITY: return COLOR_ACTIVITY;
            case EVENT: return COLOR_EVENT;
            case BLOCK: return COLOR_BLOCK;
            case FUNCTION: return COLOR_FUNCTION;
            case VARIABLE: return COLOR_VARIABLE;
            case COMPONENT: return COLOR_COMPONENT;
        }
        return 0xFF888888;
    }

    /**
     * Простая раскладка: дерево по уровням.
     * PROJECT - в центре сверху.
     * ACTIVITY - второй уровень.
     * EVENT - третий.
     * BLOCK - четвёртый и дальше (вглубь).
     */
    private void layout() {
        if (graph == null) return;

        // Считаем уровни
        java.util.Map<String, Integer> level = new java.util.HashMap<>();
        for (ProjectGraphNode n : graph.nodes) {
            level.put(n.id, getDepth(n.id));
        }

        // Группируем узлы по уровням
        java.util.Map<Integer, java.util.List<ProjectGraphNode>> byLevel = new java.util.TreeMap<>();
        for (ProjectGraphNode n : graph.nodes) {
            int lvl = level.getOrDefault(n.id, 0);
            byLevel.computeIfAbsent(lvl, k -> new java.util.ArrayList<>()).add(n);
        }

        // Раскладываем по уровням
        float y = 0;
        for (java.util.Map.Entry<Integer, java.util.List<ProjectGraphNode>> entry : byLevel.entrySet()) {
            java.util.List<ProjectGraphNode> nodes = entry.getValue();
            float totalWidth = nodes.size() * NODE_WIDTH + (nodes.size() - 1) * (LEVEL_GAP_X - NODE_WIDTH);
            float x = -totalWidth / 2f;
            for (ProjectGraphNode n : nodes) {
                n.x = x;
                n.y = y;
                x += LEVEL_GAP_X;
            }
            y += LEVEL_GAP_Y;
        }
    }

    private int getDepth(String nodeId) {
        int depth = 0;
        ProjectGraphNode current = graph.findNode(nodeId);
        while (current != null && current.parentId != null) {
            depth++;
            current = graph.findNode(current.parentId);
        }
        return depth;
    }

    private float computeGraphWidth() {
        if (graph == null) return 1000f;
        float minX = Float.MAX_VALUE, maxX = -Float.MAX_VALUE;
        for (ProjectGraphNode n : graph.nodes) {
            if (n.x < minX) minX = n.x;
            if (n.x + NODE_WIDTH > maxX) maxX = n.x + NODE_WIDTH;
        }
        return maxX - minX;
    }

    private void handleTap(float screenX, float screenY) {
        if (graph == null || tapListener == null) return;
        float x = (screenX - translateX) / scale;
        float y = (screenY - translateY) / scale;
        for (ProjectGraphNode n : graph.nodes) {
            if (!visibleTypes.contains(n.type)) continue;
            if (x >= n.x && x <= n.x + NODE_WIDTH && y >= n.y && y <= n.y + NODE_HEIGHT) {
                tapListener.onNodeTap(n);
                return;
            }
        }
    }
}
