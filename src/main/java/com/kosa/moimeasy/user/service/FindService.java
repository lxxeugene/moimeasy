package com.kosa.moimeasy.user.service;

import com.kosa.moimeasy.user.dto.PasswordResetRequest;
import com.kosa.moimeasy.user.dto.PasswordResetResponse;
import com.kosa.moimeasy.user.exception.InvalidPasswordResetException;
import com.kosa.moimeasy.user.exception.PasswordMismatchException;
import com.kosa.moimeasy.user.exception.UserNotFoundException;

public interface FindService {
    String findEmailByNicknameAndPhone(String nickname, String phone) throws UserNotFoundException;
    boolean findUserByNicknameAndPhoneAndEmail(String nickname, String phoneNumber, String email) throws UserNotFoundException;
    PasswordResetResponse resetPassword(PasswordResetRequest request) throws UserNotFoundException, PasswordMismatchException, InvalidPasswordResetException;
}
