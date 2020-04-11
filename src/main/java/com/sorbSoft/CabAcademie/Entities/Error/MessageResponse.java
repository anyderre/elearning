package com.sorbSoft.CabAcademie.Entities.Error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor(staticName="of")
public class MessageResponse {
    private String message;
}
