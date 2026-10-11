package pro.sketchware.activities.projectgraph;

import java.util.List;
import java.util.Map;

import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ProjectFileBean;

/**
 * Строит граф проекта из данных ProjectAnalyzer.
 *
 * Структура:
 *   PROJECT
 *    ├── ACTIVITY (MainActivity)
 *    │    ├── EVENT (button1_onClick)
 *    │    │    ├── BLOCK (setText)
 *    │    │    └── BLOCK (if)
 *    │    │         └── BLOCK (...)
 *    │    └── EVENT (edit1_onTextChanged)
 *    │         └── ...
 *    └── ACTIVITY (Page2Activity)
 *         └── ...
 */
public class ProjectGraphBuilder {

    private final ProjectAnalyzer analyzer;
    private final ProjectGraph graph = new ProjectGraph();

    // Корневой узел
    private static final String ROOT_ID = "project_root";

    public ProjectGraphBuilder(ProjectAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public ProjectGraph build(String projectName) {
        // 1. Корень
        String rootLabel = (projectName == null || projectName.isEmpty())
                ? "Project" : projectName;
        ProjectGraphNode root = new ProjectGraphNode(
                ROOT_ID, rootLabel, ProjectGraphNode.Type.PROJECT, null);
        graph.addNode(root);

        // 2. Activity
        List<ProjectFileBean> activities = analyzer.collectActivities();
        for (ProjectFileBean pfb : activities) {
            if (pfb == null) continue;
            String javaName = pfb.getJavaName();
            if (javaName == null || javaName.isEmpty()) continue;

            String activityId = "act_" + javaName;
            String activityLabel = javaName.replace(".java", "");
            ProjectGraphNode actNode = new ProjectGraphNode(
                    activityId, activityLabel,
                    ProjectGraphNode.Type.ACTIVITY, ROOT_ID);
            actNode.extra1 = javaName;
            graph.addNode(actNode);
            graph.addEdge(ROOT_ID, activityId, "has");

            // 3. События
            Map<String, List<BlockBean>> events = analyzer.collectEvents(javaName);
            for (Map.Entry<String, List<BlockBean>> entry : events.entrySet()) {
                String eventKey = entry.getKey();
                String eventId = "evt_" + javaName + "_" + eventKey;
                ProjectGraphNode evtNode = new ProjectGraphNode(
                        eventId, eventKey,
                        ProjectGraphNode.Type.EVENT, activityId);
                evtNode.extra1 = javaName;
                evtNode.extra2 = eventKey;
                graph.addNode(evtNode);
                graph.addEdge(activityId, eventId, "has");

                // 4. Блоки события
                List<BlockBean> blocks = entry.getValue();
                if (blocks == null || blocks.isEmpty()) continue;

                // Первый блок — «голова» цепочки (обычно index=0 или первый без nextBlock)
                for (int i = 0; i < blocks.size(); i++) {
                    BlockBean b = blocks.get(i);
                    if (b == null) continue;
                    String blockId = "blk_" + javaName + "_" + eventKey + "_" + i;
                    String blockLabel = getBlockLabel(b);
                    ProjectGraphNode blockNode = new ProjectGraphNode(
                            blockId, blockLabel,
                            ProjectGraphNode.Type.BLOCK, eventId);
                    graph.addNode(blockNode);
                    if (i == 0) {
                        graph.addEdge(eventId, blockId, "start");
                    } else {
                        BlockBean prev = blocks.get(i - 1);
                        String prevId = "blk_" + javaName + "_" + eventKey + "_" + (i - 1);
                        graph.addEdge(prevId, blockId, "next");
                    }
                }
            }

            // 5. Переменные
            List<ProjectAnalyzer.VariableInfo> variables = analyzer.collectVariables(javaName);
            for (ProjectAnalyzer.VariableInfo v : variables) {
                String varId = "var_" + javaName + "_" + v.name;
                ProjectGraphNode varNode = new ProjectGraphNode(
                        varId, v.name + " : " + v.getTypeName(),
                        ProjectGraphNode.Type.VARIABLE, activityId);
                graph.addNode(varNode);
                graph.addEdge(activityId, varId, "has");
            }

            // 6. Компоненты
            List<ProjectAnalyzer.ComponentInfo> components = analyzer.collectComponents(javaName);
            for (ProjectAnalyzer.ComponentInfo c : components) {
                String compId = "cmp_" + javaName + "_" + c.name;
                ProjectGraphNode cmpNode = new ProjectGraphNode(
                        compId, c.name + " (" + c.typeName + ")",
                        ProjectGraphNode.Type.COMPONENT, activityId);
                graph.addNode(cmpNode);
                graph.addEdge(activityId, compId, "has");
            }

            // 7. Функции (More Blocks)
            List<ProjectAnalyzer.FunctionInfo> functions = analyzer.collectFunctions(javaName);
            for (ProjectAnalyzer.FunctionInfo f : functions) {
                String fnId = "fn_" + javaName + "_" + f.name;
                ProjectGraphNode fnNode = new ProjectGraphNode(
                        fnId, f.name,
                        ProjectGraphNode.Type.FUNCTION, activityId);
                graph.addNode(fnNode);
                graph.addEdge(activityId, fnId, "has");
            }
        }

        return graph;
    }

    private String getBlockLabel(BlockBean bean) {
        if (bean.spec != null && !bean.spec.isEmpty()) return bean.spec;
        if (bean.opCode != null && !bean.opCode.isEmpty()) return bean.opCode;
        if (bean.typeName != null && !bean.typeName.isEmpty()) return bean.typeName;
        return "block";
    }

    public ProjectGraph getGraph() {
        return graph;
    }
}
