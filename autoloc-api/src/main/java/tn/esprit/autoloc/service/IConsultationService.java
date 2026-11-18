package tn.esprit.autoloc.service;

import java.util.List;

public interface IConsultationService<T, I> {
    T findById(I id);
    List<T> findAll();
    boolean check(T entity, boolean isNew);
    boolean hasVehicules(I id);
}