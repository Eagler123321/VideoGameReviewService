package com.example.videogamereviewservice.service.noImp;

import com.example.videogamereviewservice.dto.request.base.VoteRequestDto;
import com.example.videogamereviewservice.dto.response.VoteResponseDto;

import java.util.List;

public interface VoteService {
    VoteResponseDto createVote(VoteRequestDto voteRequestDto);

    VoteResponseDto updateVoteById(VoteRequestDto voteRequestDto, Long id);

    VoteResponseDto getVoteById(Long id);

    List<VoteResponseDto> getVotes();

    void deleteVoteById(Long id);
}
