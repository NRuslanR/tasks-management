package edu.examples.todos.usecases.common.extensions;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class UnpagedSorted implements Pageable
{
    private final Sort sort;

    @Override
    public boolean isPaged()
    {
        return false;
    }

    @Override
    public int getPageNumber()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPageSize()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getOffset()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sort getSort()
    {
        return sort;
    }

    @Override
    public Pageable next()
    {
        return this;
    }

    @Override
    public Pageable previousOrFirst()
    {
        return this;
    }

    @Override
    public Pageable first()
    {
        return this;
    }

    @Override
    public Pageable withPage(int pageNumber)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPrevious()
    {
        return false;
    }
}
