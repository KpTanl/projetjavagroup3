package com.group3.carrental.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group3.carrental.entity.Parking;
import com.group3.carrental.repository.ParkingRepository;

@Service
public class ParkingService {
    @Autowired
    private ParkingRepository parkingRepository;

    public List<Parking> getAllParkings() {
        return parkingRepository.findAll();
    }

    public Parking getParkingById(Long id) {
        return parkingRepository.findById(id).orElse(null);
    }

    public void save(Parking p) {
        parkingRepository.save(p);
    }

    public List<Parking> getParkingsParVille(String VilleP) {
        return parkingRepository.findByVilleP(VilleP);

    }
}