package edu.examples.todos.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class HierarchicalItem<T>
{
    private T item;
    private List<HierarchicalItem<T>> subItems;

    public HierarchicalItem(T item)
    {
        this();

        this.item = item;
    }

    public HierarchicalItem()
    {
        subItems = new ArrayList<>();
    }
}
