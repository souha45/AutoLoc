package tn.esprit.autoloc.service;

public interface ISuppressionService<I> {
    void deleteById(I id);
}