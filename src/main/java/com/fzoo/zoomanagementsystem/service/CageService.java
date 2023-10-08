package com.fzoo.zoomanagementsystem.service;

import com.fzoo.zoomanagementsystem.dto.CageRequest;
import com.fzoo.zoomanagementsystem.dto.CageViewDTO;
import com.fzoo.zoomanagementsystem.exception.UserNotFoundException;
import com.fzoo.zoomanagementsystem.model.Animal;
import com.fzoo.zoomanagementsystem.model.Area;
import com.fzoo.zoomanagementsystem.model.Cage;
import com.fzoo.zoomanagementsystem.model.Staff;
import com.fzoo.zoomanagementsystem.repository.AnimalRepository;
import com.fzoo.zoomanagementsystem.repository.AreaRepository;
import com.fzoo.zoomanagementsystem.repository.CageRepository;
import com.fzoo.zoomanagementsystem.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CageService {

    private final CageRepository cageRepository;
    private final AreaRepository areaRepository;
    private final StaffRepository staffRepository;
    private final AnimalRepository animalRepository;

    public List<CageViewDTO> getAllCages() {
        List<Cage> cageList = cageRepository.findAll(Sort.by(Sort.Direction.ASC, "areaId"));
        List<CageViewDTO> listCageView = new ArrayList<>();
        for (Cage cage : cageList) {
            listCageView.add(new CageViewDTO(
                    cage.getId(),
                    cage.getName(),
                    cage.getQuantity(),
                    cage.getCageStatus(),
                    cage.getCageType(),
                    cage.getArea().getName(),
                    cage.getStaff().getEmail()));
        }
        return listCageView;
    }

    public void addNewCage(CageRequest request) throws UserNotFoundException {
        Area area = areaRepository.findAreaByName(request.getAreaName());
        Staff staff;
        if (request.getStaffEmail().isBlank()) {
            staff = staffRepository.findStaffById(1);
        } else {
            staff = staffRepository.findStaffByEmail(request.getStaffEmail());
            if (staff == null) {
                throw new UserNotFoundException("Staff with email " + request.getStaffEmail() + " does not exist!");
            }
        }
        Cage cage = new Cage();
        cage.setName(request.getCageName());
        cage.setQuantity(0);
        cage.setCageStatus(request.getCageStatus());
        cage.setCageType(request.getCageType());
        cage.setAreaId(area.getId());
        cage.setStaffId(staff.getId());
        cage.setArea(area);
        cage.setStaff(staff);
        cageRepository.save(cage);
    }

    public Cage getCageById(int cageId) {
        return cageRepository.findCageById(cageId);
    }


    public void updateCage(int cageId, CageRequest request) throws UserNotFoundException {
        Cage cage = cageRepository.findCageById(cageId);
        if (request.getCageStatus().equals("Empty")) {
            List<Animal> animalList = animalRepository.findBycageId(cageId);
            if (!animalList.isEmpty()) {
                throw new IllegalStateException("Can not update Cage Status to empty because there are animals in cage");
            }
        }
        Staff staff = staffRepository.findStaffByEmail(request.getStaffEmail());
        if (staff == null) {
            throw new UserNotFoundException("Staff with email " + request.getStaffEmail() + " does not exist!");
        }
        Area area = areaRepository.findAreaByName(request.getAreaName());
        cage.setName(request.getCageName());
        cage.setCageStatus(request.getCageStatus());
        cage.setCageType(request.getCageType());
        cage.setArea(area);
        cage.setAreaId(area.getId());
        cage.setStaff(staff);
        cage.setStaffId(staff.getId());
        cageRepository.save(cage);
    }

}