package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.ClienDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<ClienDTO> getClientsDTO() {
        return clientRepository.findAll().stream().map(client -> new ClienDTO(client)).collect(Collectors.toList());
    }

    @Override
    public ClienDTO getIdClientDTO(Long id) {
        return clientRepository.findById(id).map(client -> new ClienDTO(client)).orElse(null);
    }

    @Override
    public Client clientFindByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
