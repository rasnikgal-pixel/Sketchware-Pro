package mod.jbk.util;

import com.besome.sketch.beans.BlockBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Сериализатор цепочки блоков в JSON-структуру (для сохранения пользовательских сборок).
 * Обходит nextBlock, subStack1, subStack2 рекурсивно.
 */
public final class BlockChainSerializer {

    private BlockChainSerializer() {}

    /**
     * Сериализует цепочку блоков начиная с указанного.
     *
     * @param start     стартовый блок (точка входа)
     * @param allBlocks все блоки на холсте
     * @return Map-структура для записи в JSON
     */
    public static Map<String, Object> serialize(BlockBean start, List<BlockBean> allBlocks) {
        if (start == null) return null;
        return serializeRecursive(start, allBlocks);
    }

    private static Map<String, Object> serializeRecursive(BlockBean block, List<BlockBean> allBlocks) {
        if (block == null) return null;
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", block.type == null ? " " : block.type);
        result.put("opCode", block.opCode == null ? "" : block.opCode);
        if (block.spec != null && !block.spec.isEmpty()) {
            result.put("spec", block.spec);
        }
        if (block.typeName != null && !block.typeName.isEmpty()) {
            result.put("typeName", block.typeName);
        }
        if (block.parameters != null && !block.parameters.isEmpty()) {
            result.put("parameters", new ArrayList<>(block.parameters));
        }

        BlockBean next = findById(allBlocks, block.nextBlock);
        if (next != null) {
            result.put("nextBlock", serializeRecursive(next, allBlocks));
        }
        BlockBean sub1 = findById(allBlocks, block.subStack1);
        if (sub1 != null) {
            result.put("subStack1", serializeRecursive(sub1, allBlocks));
        }
        BlockBean sub2 = findById(allBlocks, block.subStack2);
        if (sub2 != null) {
            result.put("subStack2", serializeRecursive(sub2, allBlocks));
        }
        return result;
    }

    /**
     * Ищет BlockBean по числовому id в списке.
     * Возвращает null, если id == -1 или блок не найден.
     */
    private static BlockBean findById(List<BlockBean> allBlocks, int id) {
        if (id < 0 || allBlocks == null) return null;
        String target = String.valueOf(id);
        for (BlockBean b : allBlocks) {
            if (b == null || b.id == null) continue;
            if (target.equals(b.id)) return b;
        }
        return null;
    }
}
