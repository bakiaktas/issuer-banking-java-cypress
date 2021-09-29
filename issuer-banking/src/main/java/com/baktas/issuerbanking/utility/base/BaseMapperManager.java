package com.baktas.issuerbanking.utility.base;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseMapperManager<Model, Entity> {
    private Class<Model> modelClass;
    private Class<Entity> entityClass;

    protected BaseMapper<Model, Entity> baseMapper;

    public BaseMapperManager() {
        this.modelClass = (Class<Model>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.entityClass = (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public Model entity2Model(Entity entity) {
        return this.baseMapper.entity2Model(entity);
    }

    public List<Model> entityList2ModelList(List<Entity> entityList) {
        return this.baseMapper.entityList2ModelList(entityList);
    }

    public Entity model2Entity(Model model) {
        return this.baseMapper.model2Entity(model);
    }
}