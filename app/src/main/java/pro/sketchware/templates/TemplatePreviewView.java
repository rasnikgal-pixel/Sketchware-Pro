package pro.sketchware.templates;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Схематичный рендер макета шаблона.
 * Рисует прямоугольники по типам View из ScreenTemplate.views.
 *
 * Также показывает стрелки ← → и счётчик «X / N».
 */
public class TemplatePreviewView extends View {

    public interface OnPageChangeListener {
        void onPageChanged(int newIndex, int total);
        void onPrevClicked();
        void onNextClicked();
    }

    // Цвета (нейтральные)
    private static final int COLOR_BG = 0xFF2A2A2A;
    private static final int COLOR_TEXT = 0xFF9E9E9E;
    private static final int COLOR_EDIT = 0xFF616161;
    private static final int COLOR_BUTTON = 0xFF6750A4;
    private static final int COLOR_IMAGE = 0xFF424242;
    private static final int COLOR_LIST = 0xFF4A4A4A;
    private static final int COLOR_ARROW = 0xFFFFFFFF;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ScreenTemplate template;
    private int pageIndex = 0;
    private int pageTotal = 1;

    private RectF prevRect = new RectF();
    private RectF nextRect = new RectF();

    // Отступы под панели (задаются из AddViewActivity):
    // top    — statusbar + toolbar
    // left   — drawer
    // bottom — fab + keyboard
    private int insetTop = 0;
    private int insetLeft = 0;
    private int insetBottom = 0;

    public void setInsets(int topPx, int leftPx, int bottomPx) {
        if (insetTop == topPx && insetLeft == leftPx && insetBottom == bottomPx) return;
        this.insetTop = topPx;
        this.insetLeft = leftPx;
        this.insetBottom = bottomPx;
        invalidate();
    }

    private OnPageChangeListener listener;

    public TemplatePreviewView(Context context) {
        super(context);
        init();
    }

    public TemplatePreviewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TemplatePreviewView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        textPaint.setColor(COLOR_ARROW);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(dp(14));
        setWillNotDraw(false);
    }

    public void setOnPageChangeListener(OnPageChangeListener l) {
        this.listener = l;
    }

    public void setTemplate(ScreenTemplate t, int index, int total) {
        pro.sketchware.smartdrop.DebugLogger.get(getContext()).i("TemplatePreviewView",
                "log_template_set",
                (t != null ? t.id : "null") + ", index=" + index + "/" + total);
        this.template = t;
        this.pageIndex = index;
        this.pageTotal = total;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        pro.sketchware.smartdrop.DebugLogger.get(getContext()).i("TemplatePreviewView",
                "log_template_draw",
                "w=" + w + ", h=" + h
                        + ", tpl=" + (template != null ? template.id : "null")
                        + ", views=" + (template != null && template.views != null ? template.views.size() : -1));

        // Фон — вся область
        paint.setColor(COLOR_BG);
        canvas.drawRect(0, 0, w, h, paint);

        // Область рендера макета: под statusbar/toolbar, над fab/keyboard, справа от drawer
        int x0 = insetLeft;
        int y0 = insetTop;
        int x1 = w;
        int y1 = h - insetBottom;
        if (x1 <= x0 || y1 <= y0) {
            drawPager(canvas, w, h);
            return;
        }

        // Рендер ViewBeans внутри области
        if (template != null && template.views != null && !template.views.isEmpty()) {
            canvas.save();
            canvas.clipRect(x0, y0, x1, y1);
            canvas.translate(x0, y0);
            renderViews(canvas, template.views, x1 - x0, y1 - y0);
            canvas.restore();
        }

        // Стрелки и счётчик — поверх всего
        drawPager(canvas, w, h);
    }

    private void renderViews(Canvas canvas, List<ScreenTemplate.RawView> views, int w, int h) {
        float cursorY = dp(8);

        for (ScreenTemplate.RawView v : views) {
            float marginL = dp(v.marginLeft);
            float marginR = dp(v.marginRight);
            float marginT = dp(v.marginTop);

            float x = marginL;
            float y = cursorY + marginT;

            // Ширина
            float width;
            if (v.width == -1 || v.width == -2) width = w - marginL - marginR;
            else width = dp(v.width);

            // Высота
            float height;
            if (v.height == -1) height = dp(60);
            else if (v.height == -2) height = dp(32);
            else height = dp(v.height);

            if (y + height > h) break;

            if (v.layoutGravity == 1 || v.layoutGravity == 17) {
                x = (w - width) / 2f;
            }

            drawView(canvas, v.type, x, y, width, height);
            cursorY = y + height + dp(4);
        }
    }

    private void drawView(Canvas canvas, int type, float x, float y, float w, float h) {
        switch (type) {
            case 4:  // TextView
            case 3:  // Button
                if (type == 3) {
                    paint.setColor(COLOR_BUTTON);
                    canvas.drawRoundRect(x, y, x + w, y + h, dp(4), dp(4), paint);
                } else {
                    // TextView — полоска
                    paint.setColor(COLOR_TEXT);
                    float lineH = Math.min(h, dp(12));
                    canvas.drawRoundRect(x, y + (h - lineH) / 2f, x + w, y + (h + lineH) / 2f,
                            dp(2), dp(2), paint);
                }
                break;
            case 5:  // EditText
                paint.setColor(COLOR_EDIT);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(dp(1.5f));
                canvas.drawRoundRect(x, y, x + w, y + h, dp(4), dp(4), paint);
                paint.setStyle(Paint.Style.FILL);
                break;
            case 6:  // ImageView
                paint.setColor(COLOR_IMAGE);
                canvas.drawRoundRect(x, y, x + w, y + h, dp(4), dp(4), paint);
                // Иконка внутри
                paint.setColor(COLOR_TEXT);
                float iconSize = Math.min(w, h) * 0.4f;
                canvas.drawCircle(x + w / 2f, y + h / 2f, iconSize / 2f, paint);
                break;
            case 9:  // ListView
                paint.setColor(COLOR_LIST);
                canvas.drawRoundRect(x, y, x + w, y + h, dp(4), dp(4), paint);
                // Полоски внутри
                paint.setColor(COLOR_TEXT);
                float lineGap = h / 6f;
                for (int i = 1; i <= 4; i++) {
                    float ly = y + lineGap * i;
                    canvas.drawRect(x + dp(8), ly - dp(1), x + w - dp(8), ly + dp(1), paint);
                }
                break;
            case 7:  // WebView
                paint.setColor(COLOR_EDIT);
                canvas.drawRoundRect(x, y, x + w, y + h, dp(4), dp(4), paint);
                break;
            case 8:  // ProgressBar
                paint.setColor(COLOR_TEXT);
                canvas.drawCircle(x + w / 2f, y + h / 2f, Math.min(w, h) / 4f, paint);
                break;
            case 11: // CheckBox
            case 13: // Switch
                paint.setColor(COLOR_TEXT);
                float boxSize = Math.min(h, dp(16));
                canvas.drawRoundRect(x, y + (h - boxSize) / 2f, x + boxSize, y + (h + boxSize) / 2f,
                        dp(2), dp(2), paint);
                // Линия текста справа
                canvas.drawRect(x + boxSize + dp(8), y + h / 2f - dp(3),
                        x + w, y + h / 2f + dp(3), paint);
                break;
            case 16: // FAB
                paint.setColor(0xFFE91E63);
                canvas.drawCircle(x + w / 2f, y + h / 2f, Math.min(w, h) / 2f, paint);
                break;
            default:
                // Заглушка
                paint.setColor(COLOR_EDIT);
                canvas.drawRoundRect(x, y, x + w, y + h, dp(2), dp(2), paint);
        }
    }

    private void drawPager(Canvas canvas, int w, int h) {
        // Счётчик "X / N" — по центру
        String counter = (pageIndex + 1) + " / " + pageTotal;
        textPaint.setColor(COLOR_ARROW);
        textPaint.setTextSize(dp(13));
        canvas.drawText(counter, w / 2f, dp(20), textPaint);

        // Стрелки ← → (слева и справа)
        float arrowSize = dp(20);
        float padding = dp(4);

        textPaint.setTextSize(dp(18));
        // ←
        prevRect.set(padding, dp(2), padding + arrowSize, dp(2) + arrowSize);
        canvas.drawText("‹", prevRect.centerX(), prevRect.centerY() + dp(7), textPaint);

        // →
        nextRect.set(w - padding - arrowSize, dp(2), w - padding, dp(2) + arrowSize);
        canvas.drawText("›", nextRect.centerX(), nextRect.centerY() + dp(7), textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            if (prevRect.contains(x, y)) {
                if (listener != null) listener.onPrevClicked();
                return true;
            }
            if (nextRect.contains(x, y)) {
                if (listener != null) listener.onNextClicked();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private float dp(float v) {
        return v * getResources().getDisplayMetrics().density;
    }
}
