package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

@Service
public class ClientServiceImpl extends GenericServiceImpl<Client, Long> implements IClientService {

    public ClientServiceImpl(IClientRepository repository) {
        super(repository);
    }

    @Override protected String getEntityName() { return "Client"; }
    @Override protected Long getId(Client c) { return c.getIdClient(); }

    @Override
    protected void setFields(Client source, Client target) {
        target.setNom(source.getNom());
        //..
    }

    @Override
    public boolean check(Client c, boolean isNew) {
        return Utils.isNoEmpty(c.getNom()); // + les champs obligatoires
    }
}
