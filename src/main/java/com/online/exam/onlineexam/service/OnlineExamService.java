package com.online.exam.onlineexam.service;

import com.online.exam.onlineexam.exceptions.UserAlreadyExist;
import com.online.exam.onlineexam.exceptions.UserDataException;
import com.online.exam.onlineexam.exceptions.UserNotFoundException;
import com.online.exam.onlineexam.model.entities.UserDetails;
import com.online.exam.onlineexam.model.requests.UserDetailsReq;
import com.online.exam.onlineexam.model.responses.UserDetailsRes;
import com.online.exam.onlineexam.repository.OnlineExamRepository;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OnlineExamService {
    private final OnlineExamRepository examRepository;

    public OnlineExamService(OnlineExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public List<UserDetailsRes> getAllUsers() throws UserNotFoundException {
        List<UserDetails> userDetailsList = examRepository.getAllUsers();
        if (userDetailsList.isEmpty()) {
            throw new UserNotFoundException("No users registered till date");
        }
        return userDetailsList.stream().map(this::convertEntityToRes).collect(Collectors.toList());
    }

    public UserDetailsRes registerUserDetails(UserDetailsReq userDetailsReq) throws UserAlreadyExist, UserDataException {
        UserDetails details = examRepository.findByPhoneNo(userDetailsReq);
        UserDetailsRes returnObj;
        if(details == null) {
            UserDetails userDetails = new UserDetails();
            userDetails.setEmail(userDetailsReq.getEmail());
            userDetails.setFirstName(userDetailsReq.getFirstName());
            userDetails.setLastName(userDetailsReq.getLastName());
            userDetails.setPhoneNo(userDetailsReq.getMobileNo());
            userDetails.setAlternateMobileNo(userDetailsReq.getAltMobileNo());
            userDetails.setSpecialization(userDetailsReq.getSpecialization());
            userDetails.setQualification(userDetailsReq.getQualification());
            userDetails.setGender(userDetailsReq.getGender());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                userDetails.setDob(dateFormat.parse(userDetailsReq.getDob()));
            } catch (ParseException ignored) {

            }
            boolean inserted = examRepository.registerUserDetails(userDetails);
            returnObj = convertEntityToRes(examRepository.findByPhoneNo(userDetailsReq));
        } else {
            LocalDate current = LocalDate.now();
            String previous = new SimpleDateFormat("yyyy-MM-dd").format(details.getRegisteredDate());
            String[] dates = previous.split("-");
            LocalDate registeredDate = LocalDate.of(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]),
                    Integer.parseInt(dates[2]));
            long days = ChronoUnit.DAYS.between(registeredDate, current);
            if (days > 30) {
                details.setRegisteredDate(new Date());
                boolean inserted = examRepository.registerUserDetails(details);
                returnObj = convertEntityToRes(examRepository.getById(details.getId()));
            } else {
                throw new UserAlreadyExist("User Already exist");
            }
        }
        return returnObj;
    }

    private UserDetailsRes convertEntityToRes(UserDetails details1) {
        UserDetailsRes detailsRes = new UserDetailsRes();
        detailsRes.setLastName(details1.getLastName());
        detailsRes.setFirstName(details1.getFirstName());
        detailsRes.setEmail(details1.getEmail());
        detailsRes.setMobileNo(details1.getPhoneNo());
        detailsRes.setUserId(details1.getId());
        return detailsRes;
    }
}
