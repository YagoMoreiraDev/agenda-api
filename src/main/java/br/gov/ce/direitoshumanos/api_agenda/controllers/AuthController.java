package br.gov.ce.direitoshumanos.api_agenda.controllers;

import br.gov.ce.direitoshumanos.api_agenda.dtos.LoginRequest;
import br.gov.ce.direitoshumanos.api_agenda.dtos.LoginResponse;
import br.gov.ce.direitoshumanos.api_agenda.models.UsuarioDetails;
import br.gov.ce.direitoshumanos.api_agenda.services.JwtService;
import br.gov.ce.direitoshumanos.api_agenda.services.UsuarioDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UsuarioDetailsService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        var auth = new UsernamePasswordAuthenticationToken(request.cpf(), request.senha());
        authManager.authenticate(auth);

        var userDetails = (UsuarioDetails) userService.loadUserByUsername(request.cpf());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                userDetails.getUsuario().getId(),
                userDetails.getNome(),
                userDetails.getUsername(),
                userDetails.getUsuario().isPrecisaAlterarSenha()
        );
    }
}
