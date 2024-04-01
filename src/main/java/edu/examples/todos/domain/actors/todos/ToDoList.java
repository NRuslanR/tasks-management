package edu.examples.todos.domain.actors.todos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ToDoList implements Iterable<ToDo>
{
    public static ToDoList of(Collection<ToDo> source)
    {
        return new ToDoList(source);
    }

    private Set<ToDo> internal;

    public ToDoList(Collection<ToDo> source)
    {
        internal = new HashSet<>(source);
    }

    public ToDoList()
    {
        internal = new HashSet<>();
    }

    @Override
    public Iterator<ToDo> iterator()
    {
        return internal.iterator();
    }

    public void add(ToDo toDo)
    {
        if (!internal.add(toDo))
            throw new ToDoIsAlreadyContainedInListException();
    }

    public void remove(ToDo toDo)
    {
        internal.remove(toDo);
    }

    public boolean contains(ToDo parentToDo)
    {
        return internal.contains(parentToDo);
    }
}
