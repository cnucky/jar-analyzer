package me.n1ar4.jar.analyzer.db.mapper;

import me.n1ar4.jar.analyzer.dto.ClassResult;
import me.n1ar4.jar.analyzer.entity.SpringControllerEntity;

import java.util.List;

public interface SpringControllerMapper {
    int insertControllers(List<SpringControllerEntity> controllers);

    List<ClassResult> selectAllSpringC();
}
