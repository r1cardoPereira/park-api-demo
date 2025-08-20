package com.github.r1cardopereira.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtils {


    // CONSTANTES PARA MONTAR O JWT TOKEN
    public  static final String  JWT_BEARER = "Bearer ";
    public  static final String  JWT_AUTHORIZATION = "Authorization";
    public  static final String  SECRET_KEY = "05132215405-0699486251-220075168";
    public  static final long EXPIRE_DAYS = 0;
    public  static final long  EXPIRE_HOURS = 0;
    public  static final long  EXPIRE_MINUTES = 2;

    // CONSTRUTOR VAZIO
    private JwtUtils(){
    }

    /**
     * METODO generateKey()
     * Metodo retorna um Interface Key baseado no SECRET_KEY fazendo o encode da constante
     **/

    private static Key generateKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }




    /**
     * METODO toExpireKey()
     * Retorna um objeto Date
     * Parametro um Objeto Date onde é adicionado a uma variavel LocalDateTime garantido a Timezone do sistema
     * Variavel end onde é criada montando com a variavel dateTime montada acima e adicionado as constantes EXPIRE_DAYS, EXPIRE_HOURS, EXPIRE_MINUTES adicionando a data atual os valores das constantes
     * retorna a data formatada de end seguindo o timezone do sistema
     **/
    private static Date toExpireDate(Date start){
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * Cria o token JWT onde instancia uma data e já cria cria o tempo limite
     * Depois cria um Token usando Jwts builder informando o cabecalho, subject que nesse caso é o usuario, data de criação e expiração
     * Assina o SECRET_KEY encoded no metodo generateKey() com a Criptografia HS256
     * Incica a role  e compacta  na variavel token
     * retorna uma nova instacia de JwtToken passando como parametro o token montado anteriormente
     **/
    public static JwtToken createToken(String username, String role){
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);
        String token = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .subject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(limit)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .claim("role", role)
                .compact();

        return new JwtToken(token);

    }



    private static Claims getClaimsFromToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();
        }catch (JwtException ex){
            log.error(String.format("Token inválido %s", ex.getMessage()));
        }
        return null;
    }

    // Esse remove a String "Bearer " do token e retorna o restante
    private static String refactorToken(String token){
        if(token.contains(JWT_BEARER)){
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

    // Esse retorna o Usuario no subject do token
    public static String getUsernameFormToken(String token){
        return getClaimsFromToken(token).getSubject();
    }


    // Esse verifica se o token é valido,
    public static boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token));
            return true;
        }catch (JwtException ex){
            log.error(String.format("Token inválido %s", ex.getMessage()));
        }
        return false;


    }

}
