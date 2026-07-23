package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.client.ClientResponse;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ClientService clientService;

    @Test
    void shouldReturnClientByIdWithoutErrors() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientResponse clientResponse =
                new ClientResponse(id, "Firstname", "Lastname", "email@email.com", "(00)00000-0000", LocalDate.of(2000, 1, 1), true);

        when(clientService.findById(id))
                .thenReturn(clientResponse);

        mvc.perform(
                get("/api/v1/clients/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenClientByIdDoesNotExist() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(clientService.findById(id))
                .thenThrow(new EntityNotFoundException("Client not found with ID: " + id));

        mvc.perform(
                get("/api/v1/clients/{id}", id))
                .andExpect(status().isNotFound());

        verify(clientService).findById(id);
    }

    @Test
    void shouldReturnAllClients() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        ClientResponse clientResponse = new ClientResponse(
                id, "firstName",
                "Lastname",
                "cliente@email.com",
                "(00)00000-0000",
                LocalDate.of(2000, 7, 22),
                true);

        PageImpl<ClientResponse> page = new PageImpl<>(List.of(clientResponse));

        when(clientService.findAll(any(), any(), any(Pageable.class)))
                .thenReturn(page);

       mvc.perform(get("/api/v1/clients")
               .param("name", "Name")
               .param("active", "true"))
               .andExpect(status().isOk());

        verify(clientService).findAll(any(), any(), any(Pageable.class));
    }
}