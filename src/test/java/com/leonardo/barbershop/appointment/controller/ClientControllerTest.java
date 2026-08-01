package com.leonardo.barbershop.appointment.controller;

import com.leonardo.barbershop.appointment.dto.client.ClientPatchRequest;
import com.leonardo.barbershop.appointment.dto.client.ClientRequest;
import com.leonardo.barbershop.appointment.dto.client.ClientResponse;
import com.leonardo.barbershop.appointment.dto.client.ClientUpdateRequest;
import com.leonardo.barbershop.appointment.exception.EmailAlreadyRegisteredException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyActivatedException;
import com.leonardo.barbershop.appointment.exception.EntityAlreadyDeactivatedException;
import com.leonardo.barbershop.appointment.exception.EntityNotFoundException;
import com.leonardo.barbershop.appointment.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnClientByIdWithoutErrors() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientResponse clientResponse =
                new ClientResponse(id, "Firstname", "Lastname", "email@email.com", "(00)00000-0000", LocalDate.of(2000, 1, 1), true);

        when(clientService.findById(id))
                .thenReturn(clientResponse);

        mvc.perform(
                get("/api/v1/clients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("Firstname"))
                .andExpect(jsonPath("$.email").value("email@email.com"));
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
                id,
                "firstName",
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
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].id").value(id.toString()))
               .andExpect(jsonPath("$.content[0].firstName").value("firstName"))
               .andExpect(jsonPath("$.content[0].email").value("cliente@email.com"));

        verify(clientService).findAll(any(), any(), any(Pageable.class));
    }

    @Test
    void shouldCreateClient() throws Exception {
        ClientRequest clientRequest = new ClientRequest("Firstname", "Lastname", "client@email.com", "(11) 90000-0000", LocalDate.of(2002, 1, 2));

        when(clientService.create(clientRequest))
                .thenReturn(new ClientResponse(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"), "Firstname", "Lastname", "client@email.com", "(11) 90000-0000", LocalDate.of(2002, 1, 2), true));

        mvc.perform(
                post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
                .andExpect(jsonPath("$.firstName").value("Firstname"))
                .andExpect(jsonPath("$.email").value("client@email.com"));

        verify(clientService).create(clientRequest);
    }

    @Test
    void shouldUpdateClient() throws Exception {
        ClientUpdateRequest clientUpdateRequest = new ClientUpdateRequest(
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2)
        );


        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientResponse clientResponse = new ClientResponse(id,
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2),
                true
        );

        when(clientService.update(id, clientUpdateRequest))
                .thenReturn(clientResponse);

        mvc.perform(
                put("/api/v1/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientUpdateRequest))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("Firstname"))
                .andExpect(jsonPath("$.email").value("client@email.com"));

        verify(clientService).update(id, clientUpdateRequest);
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientUpdateRequest clientUpdateRequest = new ClientUpdateRequest(
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2)
        );

        when(clientService.update(id, clientUpdateRequest))
                .thenThrow(new EntityNotFoundException("Client not found with ID: " + id));

        mvc.perform(
                put("/api/v1/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientUpdateRequest))
        ).andExpect(status().isNotFound());

        verify(clientService).update(id, clientUpdateRequest);
    }

    @Test
    void shouldPartiallyUpdateClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientPatchRequest clientPatchRequest = new ClientPatchRequest(
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2)
        );

        ClientResponse clientResponse = new ClientResponse(id,
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2),
                true
        );

        when(clientService.partialUpdate(id, clientPatchRequest))
                .thenReturn(clientResponse);

        mvc.perform(
                patch("/api/v1/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientPatchRequest))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("Firstname"))
                .andExpect(jsonPath("$.email").value("client@email.com"));

        verify(clientService).partialUpdate(id, clientPatchRequest);
    }

    @Test
    void shouldReturnNotFoundWhenPartiallyUpdatingNonexistentClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientPatchRequest clientPatchRequest = new ClientPatchRequest(
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2)
        );

        when(clientService.partialUpdate(id, clientPatchRequest))
                .thenThrow(new EntityNotFoundException("Client not found with ID: " + id));

        mvc.perform(
                patch("/api/v1/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientPatchRequest))
        ).andExpect(status().isNotFound());

        verify(clientService).partialUpdate(id, clientPatchRequest);
    }

    @Test
    void shouldDeactivateClient() throws Exception{
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientResponse clientResponse = new ClientResponse(id,
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2),
                false
        );

        when(clientService.deactivate(id))
                .thenReturn(clientResponse);

        mvc.perform(
                patch("/api/v1/clients/{id}/deactivate", id)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("Firstname"))
                .andExpect(jsonPath("$.email").value("client@email.com"))
                .andExpect(jsonPath("$.active").value(false));

        verify(clientService).deactivate(id);
    }

    @Test
    void shouldReturnNotFoundWhenDeactivatingNonexistentClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(clientService.deactivate(id))
                .thenThrow(new EntityNotFoundException("Client not found with ID: " + id));

        mvc.perform(
                patch("/api/v1/clients/{id}/deactivate", id)
        ).andExpect(status().isNotFound());

        verify(clientService).deactivate(id);
    }

    @Test
    void shouldActivateClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientResponse clientResponse = new ClientResponse(id,
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2),
                true
        );

        when(clientService.activate(id))
                .thenReturn(clientResponse);

        mvc.perform(
                patch("/api/v1/clients/{id}/activate", id)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("Firstname"))
                .andExpect(jsonPath("$.email").value("client@email.com"))
                .andExpect(jsonPath("$.active").value(true));

        verify(clientService).activate(id);
    }

    @Test
    void shouldReturnNotFoundWhenActivatingNonexistentClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(clientService.activate(id))
                .thenThrow(new EntityNotFoundException("Client not found with ID: " + id));

        mvc.perform(
                patch("/api/v1/clients/{id}/activate", id)
        ).andExpect(status().isNotFound());

        verify(clientService).activate(id);
    }

    @Test
    void shouldReturnConflictWhenActivatingAlreadyActiveClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(clientService.activate(id))
                .thenThrow(new EntityAlreadyActivatedException("Client already activated"));

        mvc.perform(
                patch("/api/v1/clients/{id}/activate", id)
        ).andExpect(status().isConflict());

        verify(clientService).activate(id);
    }

    @Test
    void shouldReturnConflictWhenDeactivatingAlreadyInactiveClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(clientService.deactivate(id))
                .thenThrow(new EntityAlreadyDeactivatedException("Client already deactivated"));

        mvc.perform(
                patch("/api/v1/clients/{id}/deactivate", id)
        ).andExpect(status().isConflict());

        verify(clientService).deactivate(id);
    }

    @Test
    void shouldReturnConflictWhenCreatingClientWithRegisteredEmail() throws Exception {
        ClientRequest clientRequest = new ClientRequest("Firstname", "Lastname", "client@email.com", "(11) 90000-0000", LocalDate.of(2002, 1, 2));

        when(clientService.create(clientRequest))
                .thenThrow(new EmailAlreadyRegisteredException("Email " + clientRequest.email() + " already registered!"));

        mvc.perform(
                post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest))
        ).andExpect(status().isConflict());

        verify(clientService).create(clientRequest);
    }

    @Test
    void shouldReturnConflictWhenUpdatingClientWithRegisteredEmail() throws Exception {
        ClientUpdateRequest clientUpdateRequest = new ClientUpdateRequest(
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2)
        );

        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(clientService.update(id, clientUpdateRequest))
                .thenThrow(new EmailAlreadyRegisteredException("Email " + clientUpdateRequest.email() + " already registered!"));

        mvc.perform(
                put("/api/v1/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientUpdateRequest))
        ).andExpect(status().isConflict());

        verify(clientService).update(id, clientUpdateRequest);
    }

    @Test
    void shouldReturnConflictWhenPartiallyUpdatingClientWithRegisteredEmail() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientPatchRequest clientPatchRequest = new ClientPatchRequest(
                "Firstname",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2)
        );

        when(clientService.partialUpdate(id, clientPatchRequest))
                .thenThrow(new EmailAlreadyRegisteredException("Email " + clientPatchRequest.email() + " already registered!"));

        mvc.perform(
                patch("/api/v1/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientPatchRequest))
        ).andExpect(status().isConflict());

        verify(clientService).partialUpdate(id, clientPatchRequest);
    }

    @Test
    void shouldReturnBadRequestWhenCreatingInvalidClient() throws Exception {
        ClientRequest clientRequest = new ClientRequest(
                "",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2)
        );

        mvc.perform(
                post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequest))
        ).andExpect(status().isBadRequest());

        verify(clientService, never()).create(any(ClientRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingInvalidClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientUpdateRequest clientUpdateRequest = new ClientUpdateRequest(
                "",
                "Lastname",
                "client@email.com",
                "(11) 90000-0000",
                LocalDate.of(2002, 1, 2)
        );

        mvc.perform(
                put("/api/v1/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientUpdateRequest))
        ).andExpect(status().isBadRequest());

        verify(clientService, never()).update(any(UUID.class), any(ClientUpdateRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenPartiallyUpdatingInvalidClient() throws Exception {
        UUID id = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        ClientPatchRequest clientPatchRequest = new ClientPatchRequest(
                null,
                null,
                "invalid-email",
                null,
                null
        );

        mvc.perform(
                patch("/api/v1/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientPatchRequest))
        ).andExpect(status().isBadRequest());

        verify(clientService, never()).partialUpdate(any(UUID.class), any(ClientPatchRequest.class));
    }
}
