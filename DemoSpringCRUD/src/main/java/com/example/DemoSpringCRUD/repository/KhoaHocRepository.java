package com.example.DemoSpringCRUD.repository;




import com.example.DemoSpringCRUD.model.KhoaHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoaHocRepository extends JpaRepository<KhoaHoc,Integer> {
}
