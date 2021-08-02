package com.course2go.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.course2go.model.board.Board;

public interface BoardDao extends JpaRepository<Board, Integer>  {
	public Board findAllByBid(Integer bid);
	public List<Board> findAllByBoardWriterUidIn(Iterable<String> uids);
}