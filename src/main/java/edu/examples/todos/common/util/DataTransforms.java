package edu.examples.todos.common.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class DataTransforms
{
    public static <T> List<HierarchicalItem<T>> transformFlatDataToHierarchical(
            List<T> items,
            String referencingFieldName,
            String referencedFieldName
    )
    {
        var itemsMap = new HashMap<Object, HierarchicalItem<T>>();

        for(T item: items)
        {
            var referencingFieldValue = getFieldValue(item, referencingFieldName);
            var referencedFieldValue = getFieldValue(item, referencedFieldName);

            var hierarchicalItem = itemsMap.get(referencedFieldValue);

            if (Objects.isNull(hierarchicalItem))
            {
                hierarchicalItem = new HierarchicalItem<>(item);

                itemsMap.put(referencedFieldValue, hierarchicalItem);
            }

            else
            {
                hierarchicalItem.setItem(item);
            }

            var parentHierarchicalItem = itemsMap.get(referencingFieldValue);

            if (!Objects.isNull(parentHierarchicalItem))
            {
                parentHierarchicalItem.getSubItems().add(hierarchicalItem);
            }

            else if(!ObjectUtils.isEmpty(referencingFieldValue))
            {
                itemsMap.put(
                        referencingFieldValue,
                        new HierarchicalItem<>(null, new ArrayList<>(List.of(hierarchicalItem)))
                );
            }
        }

        return
                itemsMap
                        .values()
                        .stream()
                        .filter(
                            v -> isRootItem(
                                v,
                                i -> itemsMap.get(getFieldValue(i, referencingFieldName))
                            )
                        )
                        .toList();
    }

    private static <T> boolean isRootItem(
            HierarchicalItem<T> item,
            Function<T, HierarchicalItem<T>> parentItemSelector
    )
    {
        if (Objects.isNull(item.getItem()))
            return false;

        var hierarchicalItem = parentItemSelector.apply(item.getItem());

        return Objects.isNull(hierarchicalItem) || Objects.isNull(hierarchicalItem.getItem());
    }

    @SneakyThrows
    private static Object getFieldValue(Object obj, String fieldName)
    {
        var field = obj.getClass().getDeclaredField(fieldName);

        field.setAccessible(true);

        try
        {
            return field.get(obj);
        }

        finally
        {
            field.setAccessible(false);
        }
    }
}
