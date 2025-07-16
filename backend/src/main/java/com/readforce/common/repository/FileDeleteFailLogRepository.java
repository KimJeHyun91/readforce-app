package com.readforce.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.readforce.common.entity.FileDeleteFailLog;

@Repository
public interface FileDeleteFailLogRepository extends JpaRepository<FileDeleteFailLog, Long>{

}
