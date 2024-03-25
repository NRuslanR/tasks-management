package edu.examples.todos.presentation.api.common.resources.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public abstract class LinkedRepresentationModelAssemblerSupport<T, D extends RepresentationModel<?>>
		extends RepresentationModelAssemblerSupport<T, D>
{
    public LinkedRepresentationModelAssemblerSupport(Class<?> controllerClass, Class<D> resourceType)
    {
        super(controllerClass, resourceType);
    }

    public D toModel(T entity)
    {
        var resource = instantiateModel(entity);

        setLinksToModel(resource, entity);

        return resource;
    }

    protected void setLinksToModel(D resource, T entity)
    {

    };

    @Override
    public CollectionModel<D> toCollectionModel(Iterable<? extends T> entities)
    {
        var resources = super.toCollectionModel(entities);

        setLinksToCollectionModel(resources, entities);

        return resources;
    }

    protected void setLinksToCollectionModel(CollectionModel<D> resources, Iterable<? extends T> entities)
    {

    }
}
