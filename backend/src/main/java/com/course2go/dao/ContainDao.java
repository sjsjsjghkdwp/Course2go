package com.course2go.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.course2go.model.contain.Contain;

public interface ContainDao extends JpaRepository<Contain, Integer> {
	Optional<Contain> findContainByContainRidAndContainPid(Integer containRid, Integer containPid);
	List<Contain> findAllByContainRid(Integer containRid);
	List<Contain> findAllByContainPid(Integer containPid);
	List<Contain> findAllByContainRidInAndContainPid(Iterable<Integer> containRids, Integer containPid);
	void deleteAllByContainRid(Integer rid);
	void deleteAllByContainVid(Integer vid);
}
