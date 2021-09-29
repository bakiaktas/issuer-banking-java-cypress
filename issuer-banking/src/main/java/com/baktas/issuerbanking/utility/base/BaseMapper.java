package com.baktas.issuerbanking.utility.base;

import java.util.List;

public interface BaseMapper<Model, Entity> {
    Model entity2Model(Entity entity);

    List<Model> entityList2ModelList(List<Entity> entityList);

    Entity model2Entity(Model model);
}