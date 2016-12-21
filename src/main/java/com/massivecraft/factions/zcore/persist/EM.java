package com.massivecraft.factions.zcore.persist;

import java.util.LinkedHashMap;
import java.util.Map;

public class EM {
    public static Map<Class<? extends Entity>, EntityCollection<? extends Entity>> class2Entities = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Entity> EntityCollection<T> getEntitiesCollectionForEntityClass(Class<T> entityClass) {
        return (EntityCollection<T>) class2Entities.get(entityClass);
    }

    public static void setEntitiesCollectionForEntityClass(Class<? extends Entity> entityClass, EntityCollection<? extends Entity> entities) {
        class2Entities.put(entityClass, entities);
    }

    // -------------------------------------------- //
    // ATTACH AND DETACH
    // -------------------------------------------- //
    @SuppressWarnings("unchecked")
    public static <T extends Entity> void attach(T entity) {
        ((EntityCollection<T>) getEntitiesCollectionForEntityClass(entity.getClass())).attach(entity);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> void detach(T entity) {
        ((EntityCollection<T>) getEntitiesCollectionForEntityClass(entity.getClass())).detach(entity);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> boolean attached(T entity) {
        return ((EntityCollection<T>) getEntitiesCollectionForEntityClass(entity.getClass())).attached(entity);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> boolean detached(T entity) {
        return ((EntityCollection<T>) getEntitiesCollectionForEntityClass(entity.getClass())).detached(entity);
    }

    // -------------------------------------------- //
    // DISC
    // -------------------------------------------- //
    public static void saveAllToDisc() {
        class2Entities.values().forEach(EntityCollection::saveToDisc);
    }

    public static void loadAllFromDisc() {
        class2Entities.values().forEach(EntityCollection::loadFromDisc);
    }
}
