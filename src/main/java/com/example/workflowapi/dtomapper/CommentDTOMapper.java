package com.example.workflowapi.dtomapper;

import com.example.workflowapi.dto.CommentDTO;
import com.example.workflowapi.model.Comment;
@Deprecated
public class CommentDTOMapper {
    public static CommentDTO mapToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthor(comment.getAuthor() != null ? comment.getAuthor().getUsername() : null);
        dto.setTaskId(comment.getTaskId());
        dto.setLikes(comment.getLikes());
        dto.setUnlikes(comment.getUnlikes());
        dto.setCreationDate(comment.getCreationDate());
        return dto;
    }
}
