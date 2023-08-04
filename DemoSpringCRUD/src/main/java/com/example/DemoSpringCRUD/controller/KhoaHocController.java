package com.example.DemoSpringCRUD.controller;




import com.example.DemoSpringCRUD.model.KhoaHoc;
import com.example.DemoSpringCRUD.model.User;
import com.example.DemoSpringCRUD.repository.KhoaHocRepository;
import com.example.DemoSpringCRUD.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/khoahoc")
public class KhoaHocController {
    @Autowired
    private KhoaHocRepository khoaHocRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/khoahocs/{userId}")
    public KhoaHoc addKhoaHoc(@PathVariable(value = "userId") Integer userId,
                              @RequestBody KhoaHoc khoaHoc) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "Khong tim thay"));
        khoaHoc.setUser(user);
        KhoaHoc khoaHocSave =khoaHocRepository.save(khoaHoc);
        return khoaHocSave;
    }
    @GetMapping("/khoahocs")
    public ResponseEntity<List<KhoaHoc>> getAllKhoaHoc() {

        return ResponseEntity.ok(khoaHocRepository.findAll());
    }


    @GetMapping("khoahocs/{khoahocId}")
    public KhoaHoc findEmployeeById(@PathVariable(value = "khoahocId") Integer employeeId) {
        System.out.println("Employee fetching from database:: "+employeeId);
        return khoaHocRepository.findById(employeeId).orElseThrow(() -> new IllegalStateException(
                "Khong tim thay"));

    }


    @PutMapping("khoahocs/{khoahocId}")
    public KhoaHoc updateKhoaHoc(
                                 @RequestParam(name = "userId") Integer userId,
                                 @PathVariable(value = "khoahocId") Integer khoaHocID,
                                 @RequestBody KhoaHoc khoaHocDetails) {
        KhoaHoc khoaHoc = khoaHocRepository.findById(khoaHocID)
                .orElseThrow(() -> new IllegalStateException(
                        "Khong tim thay"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "Khong tim thay"));
        khoaHoc.setName(khoaHocDetails.getName());
        khoaHoc.setDescription(khoaHocDetails.getDescription());
        khoaHoc.setOldPrice(khoaHocDetails.getOldPrice());
        khoaHoc.setPrice(khoaHocDetails.getOldPrice());
        khoaHoc.setImage(khoaHocDetails.getImage());
        khoaHoc.setUser(user);
        final KhoaHoc updatedKhoaHoc = khoaHocRepository.save(khoaHoc);
        return updatedKhoaHoc;

    }


    @DeleteMapping("khoahocs/{id}")
    public void deleteKhoaHoc(@PathVariable(value = "id") Integer khoahocId) {
        KhoaHoc khoaHoc = khoaHocRepository.findById(khoahocId).orElseThrow(() -> new IllegalStateException(
                "Khong tim thay"));
        khoaHocRepository.delete(khoaHoc);
    }

}
