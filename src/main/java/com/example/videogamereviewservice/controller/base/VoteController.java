package com.example.videogamereviewservice.controller.base;

import com.example.videogamereviewservice.dto.request.base.VoteRequestDto;
import com.example.videogamereviewservice.dto.response.VoteResponseDto;
import com.example.videogamereviewservice.service.local.VoteServiceLocal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/votes")
public class VoteController {
    private final VoteServiceLocal voteServiceLocal;

    public VoteController(VoteServiceLocal voteServiceLocal) {
        this.voteServiceLocal = voteServiceLocal;
    }

    @PostMapping
    public ResponseEntity<VoteResponseDto> createVoteById(@Valid @RequestBody VoteRequestDto voteRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(voteServiceLocal.createVote(voteRequestDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<VoteResponseDto> updateVoteById(@Valid @RequestBody VoteRequestDto voteRequestDto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(voteServiceLocal.updateVoteById(voteRequestDto, id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<VoteResponseDto> getVoteById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(voteServiceLocal.getVoteById(id));
    }
    @GetMapping
    public ResponseEntity<List<VoteResponseDto>> getVotes(){
        return ResponseEntity.status(HttpStatus.OK).body(voteServiceLocal.getVotes());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoteById(@PathVariable Long id){
        voteServiceLocal.deleteVoteById(id);
        return ResponseEntity.noContent().build();
    }
}
