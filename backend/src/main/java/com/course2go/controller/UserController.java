package com.course2go.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.course2go.authentication.AuthConstants;
import com.course2go.authentication.TokenUtils;
import com.course2go.model.user.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.course2go.model.BasicResponse;
import com.course2go.model.board.BoardMyList;
import com.course2go.model.route.RouteReadResponse;
import com.course2go.service.board.BoardService;
import com.course2go.service.image.S3Uploader;
import com.course2go.service.route.RouteService;
import com.course2go.service.user.UserDeleteService;
import com.course2go.service.user.UserEmailFindService;
import com.course2go.service.user.UserModifyService;
import com.course2go.service.user.UserPasswordFindService;
import com.course2go.service.user.UserProfileModifyService;
import com.course2go.service.user.UserProfileService;
import com.course2go.service.user.UserRegisterService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
@Api(value="user")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRegisterService userRegisterService;

    @PostMapping("/signup")
    @ApiOperation(value = "????????????")
    public Object signup(@Valid @RequestBody SignupRequest request) {
    	
        final BasicResponse result = new BasicResponse();
        HttpStatus status = HttpStatus.CONFLICT;

        int registerResult = userRegisterService.userRegister(request);
        
        switch(registerResult) {
        // ????????? ??????
        case 0:
        	result.data = "email overlap";
        	result.status = false;
        	break;
        // ????????? ??????
        case -1:
        	result.data = "nickname overlap";
        	result.status = false;
        	break;
        // ?????? ??????
        case 1:
        	result.data = "success";
        	result.status = true;
        	status = HttpStatus.OK;
        	break;
        }
        
        return new ResponseEntity<>(result, status);
    }

    @GetMapping("/signup/check/email")
	@ApiOperation(value = "?????????????????????")
	public Object checkEmail(@RequestParam("userEmail") String requestEmail){
		final BasicResponse result = new BasicResponse();
		HttpStatus status = HttpStatus.CONFLICT;

    	boolean checkEmailResult = userRegisterService.userEmailCheck(requestEmail);
    	if(!checkEmailResult){
    		// ????????? ????????? ?????? ??????
			result.data = "success";
			result.status = true;
			status = HttpStatus.OK;
		}else{
    		// ????????? ????????? ??????
			result.data = "email overlap";
			result.status = false;
		}

    	return new ResponseEntity<>(result, status);
	}

	@GetMapping("/signup/check/nickname")
	@ApiOperation(value = "?????????????????????")
	public Object checkNickname(@RequestParam("userNickname") String requestNickname){
		final BasicResponse result = new BasicResponse();
		HttpStatus status = HttpStatus.CONFLICT;

		boolean checkNicknameResult = userRegisterService.userNicknameCheck(requestNickname);
		if(!checkNicknameResult){
			// ????????? ????????? ?????? ??????
			result.data = "success";
			result.status = true;
			status = HttpStatus.OK;
		}else{
			// ????????? ????????? ??????
			result.data = "nickname overlap";
			result.status = false;
		}

		return new ResponseEntity<>(result, status);
	}

    @Autowired
    UserModifyService userModifyService;
    
    @PutMapping("/modify")
    @ApiOperation("??????????????????")
    public Object modify(@Valid @RequestBody UserModifyRequest request, @RequestHeader Map<String, Object> requestHeader, final HttpServletResponse response) {
    	final BasicResponse result = new BasicResponse();
    	HttpStatus status = HttpStatus.CONFLICT;

    	final String token = (String) requestHeader.get("authorization");
		Claims claims = TokenUtils.getClaimsFromToken(token);
		String tokenEmail = (String) claims.get("userEmail");
    	    	
    	int modifyResult = userModifyService.userModify(tokenEmail, request.getUserNickname(), request.getUserPassword());
    	
    	switch(modifyResult) {
    	// // ???????????? ????????? ????????? ????????? ???????????? ?????? ?????? 
    	case -1:
    		result.data = "unpresent user";
    		result.status = false;
    		status = HttpStatus.BAD_REQUEST;
    		break;
    	// ????????? ??????
    	case 0:
    		result.data = "nickname overlap";
    		result.status = false;
    		break;
    	// ???????????? ?????? ??????
    	case 1:
			final String updateToken = userModifyService.updateToken(tokenEmail);
			response.setHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + updateToken);
    		result.data = "success";
    		result.status = true;
    		status = HttpStatus.OK;
    		break;
    	}
    	
    	return new ResponseEntity<>(result, status);
    }
    
    @Autowired
    UserDeleteService userDeleteService;
    
    @DeleteMapping("/delete")
    @ApiOperation("????????????")
    public Object delete(@RequestHeader Map<String, Object> requestHeader) {
    	final BasicResponse result = new BasicResponse();
    	HttpStatus status = HttpStatus.BAD_REQUEST;

    	final String token = (String) requestHeader.get("authorization");
    	Claims claims = TokenUtils.getClaimsFromToken(token);
    	String tokenEmail = (String) claims.get("userEmail");

    	int deleteResult = userDeleteService.userDelete(tokenEmail);

    	switch(deleteResult) {
    	// ???????????? ????????? ????????? ???????????? ?????? ??????
    	case 0:
    		result.data = "unpresent user";
    		result.status = false;
    		break;
    	// ???????????? ??????
    	case 1:
    		result.data = "success";
    		result.status = true;
    		status = HttpStatus.OK;
    		break;
    	}
    	
    	return new ResponseEntity<>(result, status);
    }
    
    @Autowired
    UserEmailFindService userEmailFindService;
    
    @PostMapping("/find-email")
    @ApiOperation("???????????????")
    public Object findEmail(@Valid @RequestBody UserEmailFindRequest request) {
    	final BasicResponse result = new BasicResponse();
    	HttpStatus status = HttpStatus.BAD_REQUEST;
    	    	

    	String findEmailResult = userEmailFindService.userEmailFind(request.getUserNickname(), request.getUserBirthday());
    	
    	if(!findEmailResult.equals("fail")) {
    		result.data = "success";
    		result.status = true;
    		result.object = findEmailResult;
    		status = HttpStatus.OK;
    	}else {
    		result.data = "???????????? ?????? ????????? ?????????.";
    		result.status = false;
    	}
    	
    	return new ResponseEntity<>(result, status);
    }
    
    @Autowired
    UserProfileModifyService userProfileModifyService;
    
    @Autowired
	S3Uploader s3Uploader;
    
    @PutMapping("/edit")
    @ApiOperation("???????????????")
    public Object edit(@RequestHeader Map<String, Object> requestHeader, @RequestParam(required = false, value = "comment") String requestComment, @RequestParam(required = false, value = "image")MultipartFile multipartFile) {
    	final BasicResponse result = new BasicResponse();
    	HttpStatus status = HttpStatus.BAD_REQUEST;
    	String imageUrl = null;

    	final String token = (String) requestHeader.get("authorization");
    	Claims claims = TokenUtils.getClaimsFromToken(token);
    	String tokenEmail = (String) claims.get("userEmail");

    	// S3??? ?????????????????? url ????????????
		if(multipartFile != null){
			try {
				imageUrl = s3Uploader.upload(multipartFile, "profile");
				System.out.println("s3 ??????");
			} catch (IOException e) {
				System.out.println("s3??????");
				e.printStackTrace();
			}
		}

    	int editResult = userProfileModifyService.userProfileModify(tokenEmail, requestComment, imageUrl);
    	
    	switch(editResult) {
    	// ????????? ?????? ????????? ????????? ???????????? ?????? ??????
    	case 0:
    		result.data = "unpresent user";
    		result.status = false;
    		break;
    	// ????????? ?????? ??????
    	case 1:
    		result.data = "success";
    		result.status = true;
    		status = HttpStatus.OK;
    		break;
    	}
    	
    	return new ResponseEntity<>(result, status);
    }
    
    @Autowired
    UserProfileService userProfileService;
    
    @GetMapping("/profile/{userNickname}")
    @ApiOperation(value = "???????????????")
    public Object showProfile(@PathVariable("userNickname") String userNickname, @RequestHeader Map<String, Object> requestHeader) {
    	final BasicResponse result = new BasicResponse();
    	HttpStatus status = HttpStatus.BAD_REQUEST;

    	final String token = (String) requestHeader.get("authorization");
    	Claims claims = TokenUtils.getClaimsFromToken(token);
    	String tokenNickname = (String) claims.get("userNickname");
		System.out.println("????????? ?????? ??????(Controller) : " + tokenNickname + " -> " + userNickname);

		Object userProfileShowResult = userProfileService.userProfileShow(tokenNickname, userNickname);

    	if(userProfileShowResult.equals("nonexistent user")) { // ????????? ????????? ???????????? ?????? ??????
			result.data = "???????????? ?????? ????????? ?????????.";
			result.status = false;
    	}else if(userProfileShowResult.equals("fail")){ // ????????? ????????? ????????? ????????? ????????? ?????? ??? ?????? ??????
    		result.data = "????????? header ??? (????????? ????????? ????????? ?????? ??? ??????)";
    		result.status = false;
    	}else{ // ????????? ????????? ????????? ????????? ????????? ??????????????? ????????? ??????
			if(tokenNickname.equals(userNickname)){ // ?????? ????????? ????????? ??????
				result.data = "?????? ????????? ?????? ??????";
			}else{ // ?????? ????????? ????????? ??????
				result.data = "?????? ????????? ?????? ??????";
			}
			result.status = true;
			result.object = userProfileShowResult;
			status = HttpStatus.OK;
		}
    	
    	return new ResponseEntity<>(result, status);
    	
    }
    
    @Autowired
    UserPasswordFindService userPasswordFindService;
    
    @PostMapping("/find-pw")
    @ApiOperation("????????????????????????")
    public Object sendEmail(@Valid @RequestBody UserEmailRequest userEmailRequest) {
    	final BasicResponse result = new BasicResponse();
    	HttpStatus status = HttpStatus.BAD_REQUEST;

    	int findPasswordResult = userPasswordFindService.userPasswordFind(userEmailRequest);
    	
    	switch(findPasswordResult) {
    	// ???????????? ????????? ?????? ??????
    	case 0:
    		result.data = "unpresent user";
    		result.status = false;
    		break;
    	// ?????? ???????????? ?????? ??????
    	case 1:
    		result.data = "success";
    		result.status = true;
    		status = HttpStatus.OK;
    		break;
    	}
    	
    	return new ResponseEntity<>(result, status);
    }

    @Autowired
    BoardService boardService;
    
    @PostMapping("/list-post")
    @ApiOperation("?????? ??? ??? ??????")
    public Object getListRoute(@RequestHeader Map<String, Object> header) {
		String uid = TokenUtils.getUidFromToken((String)header.get("authorization"));
		BoardMyList response = boardService.getMyList(uid);
    	final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        result.object = response;
    	return new ResponseEntity<>(result, HttpStatus.OK);
    }
}