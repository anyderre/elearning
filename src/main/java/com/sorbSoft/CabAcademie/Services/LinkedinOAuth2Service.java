package com.sorbSoft.CabAcademie.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorbSoft.CabAcademie.model.linkedin.LinkedinProfile;
import com.sorbSoft.CabAcademie.model.linkedin.LinkedinUserProfile;
import com.sorbSoft.CabAcademie.model.linkedin.email.ElementsItem;
import com.sorbSoft.CabAcademie.model.linkedin.email.LinkedinEmail;
import javafx.util.Pair;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class LinkedinOAuth2Service {

    private final String LINKEDIN_PROFILE_URL = "https://api.linkedin.com/v2/me";
    private final String LINKEDIN_EMAIL_URL = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";

    public Pair<HttpStatus, LinkedinProfile> fetchгFirstNameAndLastNameProfile(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<JsonNode> responseJsonNode = restTemplate.exchange(LINKEDIN_PROFILE_URL, HttpMethod.GET, request, JsonNode.class);

        JsonNode jsonNode = responseJsonNode.getBody();
        //jsonValidate(jsonNode, getJsonSchema(LIST_TOES_JSON_SCHEMA));

        ObjectMapper obj = new ObjectMapper();

        LinkedinProfile toes = obj.treeToValue(jsonNode, LinkedinProfile.class);

        if (log.isDebugEnabled()) {
            log.debug("\n");
            log.debug("Requested Url: " + LINKEDIN_PROFILE_URL);
            log.debug("====================== Linkedin First Last Name Response JSON ====================");
            log.debug(obj.writeValueAsString(toes));
            log.debug("\n");
        }


        return new Pair<>(responseJsonNode.getStatusCode(), toes);

    }

    public LinkedinUserProfile fetchUserProfile(String accessToken) throws JsonProcessingException {
        Pair<HttpStatus, LinkedinProfile> httpStatusListPair = fetchгFirstNameAndLastNameProfile(accessToken);
        LinkedinProfile profile = httpStatusListPair.getValue();



        LinkedinUserProfile userProfile = new LinkedinUserProfile();
        userProfile.setFirstName(profile.getLocalizedFirstName());
        userProfile.setLastName(profile.getLocalizedLastName());
        userProfile.setProfilePictureUrl(profile.getProfilePicture().getDisplayImage());

        ElementsItem elementsItem = fetchгProfileEmail(accessToken);

        if(elementsItem != null && elementsItem.getHandle() != null) {
            userProfile.setEmailAddress(elementsItem.getHandle2().getEmailAddress());
        }

        return userProfile;
    }

    public ElementsItem fetchгProfileEmail(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<JsonNode> responseJsonNode = restTemplate.exchange(LINKEDIN_EMAIL_URL, HttpMethod.GET, request, JsonNode.class);

        JsonNode jsonNode = responseJsonNode.getBody();
        //jsonValidate(jsonNode, getJsonSchema(LIST_TOES_JSON_SCHEMA));

        ObjectMapper obj = new ObjectMapper();

        LinkedinEmail toes = obj.treeToValue(jsonNode, LinkedinEmail.class);

        if (log.isDebugEnabled()) {
            log.debug("\n");
            log.debug("Requested Url: " + LINKEDIN_EMAIL_URL);
            log.debug("====================== Linkedin Email Response JSON ====================");
            log.debug(obj.writeValueAsString(toes));
            log.debug("\n");
        }

        for(ElementsItem elem : toes.getElements()) {
            return elem;

        }

        return null;

    }
}