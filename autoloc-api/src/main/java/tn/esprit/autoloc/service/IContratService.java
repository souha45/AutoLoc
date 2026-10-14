package tn.esprit.autoloc.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

public interface IContratService extends IGenericService<Contrat, Long> { }

