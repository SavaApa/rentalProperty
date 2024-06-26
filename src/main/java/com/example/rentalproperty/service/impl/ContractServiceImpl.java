package com.example.rentalproperty.service.impl;

import com.example.rentalproperty.dto.ContractAfterCreatingDto;
import com.example.rentalproperty.dto.ContractCreateDto;
import com.example.rentalproperty.entity.Application;
import com.example.rentalproperty.entity.Contract;
import com.example.rentalproperty.entity.Landlord;
import com.example.rentalproperty.entity.Property;
import com.example.rentalproperty.entity.enums.ApplicationStatus;
import com.example.rentalproperty.entity.enums.TypeProperty;
import com.example.rentalproperty.exception.ContractDoesntExistException;
import com.example.rentalproperty.exception.IdNotFoundException;
import com.example.rentalproperty.exception.errorMessage.ErrorMessage;
import com.example.rentalproperty.mapper.ContractMapper;
import com.example.rentalproperty.repository.ContractRepository;
import com.example.rentalproperty.repository.LandlordRepository;
import com.example.rentalproperty.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final LandlordRepository landlordRepository;


    @Override
    public Contract getContractById(UUID id) {
        Contract contract = contractRepository.findContractById(id);
        if (contract == null) {
            throw new ContractDoesntExistException(ErrorMessage.NOT_EXIST);
        }
        return contract;
    }

    @Override
    @Transactional
    public void deleteContractById(UUID id) {
        if (!contractRepository.existsById(id)) {
            throw new ContractDoesntExistException(ErrorMessage.NOT_EXIST);
        } else {
            contractRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public ContractAfterCreatingDto createContract(ContractCreateDto contractCreateDto) {
        Contract contract = contractRepository.findContractByStartDate(contractCreateDto.getStartDate());
        if (contract != null) {
            throw new NullPointerException();
        }

        String applicationStatus = contractCreateDto.getApplicationStatus();
        ApplicationStatus status = ApplicationStatus.valueOf(applicationStatus);
        Application application = new Application();
        application.setApplicationStatus(status);

        String typeProperty = contractCreateDto.getTypeProperty();
        TypeProperty type = TypeProperty.valueOf(typeProperty);
        Property property = new Property();
        property.setTypeProperty(type);

        UUID idLandlord = contractCreateDto.getL_id();
        Landlord landlord = landlordRepository.findLandlordById(idLandlord);

        Contract entity = contractMapper.toEntity(contractCreateDto);
        entity.setApplication(application);
        entity.setProperty(property);
        entity.setLandlord(landlord);
        Contract contractAfterCreation = contractRepository.save(entity);
        return contractMapper.toDto(contractAfterCreation);
    }

    @Override
    @Transactional
    public Contract updateContract(UUID id, Contract contract) {
        Contract getContract = contractRepository.findContractById(id);
        if (getContract != null) {
            boolean changed = false;

            if (!getContract.getStartDate().equals(contract.getStartDate())) {
                getContract.setStartDate(contract.getStartDate());
                changed = true;
            }
            if (!getContract.getEndDate().equals(contract.getEndDate())) {
                getContract.setEndDate(contract.getEndDate());
                changed = true;
            }

            if (changed) {
                getContract = contractRepository.save(getContract);
            }
        } else {
            throw new IdNotFoundException(ErrorMessage.ID_NOT_FOUND);
        }
        return getContract;
    }


}
