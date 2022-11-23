package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClienDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClientService {

    public List<ClienDTO> getClientsDTO();

    public ClienDTO getIdClientDTO(Long id);

    public Client clientFindByEmail(String email);

    public void saveClient(Client client);
}
