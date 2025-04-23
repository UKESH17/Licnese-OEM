package com.htc.licenseapproval.controller;

import java.io.IOException;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.htc.licenseapproval.dto.CoursesDTO;
import com.htc.licenseapproval.dto.RequestDetailsDTO;
import com.htc.licenseapproval.dto.RequestResponseDTO;
import com.htc.licenseapproval.dto.ResponseDTO;
import com.htc.licenseapproval.entity.LicenseLogMessages;
import com.htc.licenseapproval.response.BaseResponse;
import com.htc.licenseapproval.service.RequestDetailsService;
import com.htc.licenseapproval.service.RequestHeaderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/licenseApproval/approvalRequest")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "License Approval Data Fetching Controller", description = "APIs for handling license approval requests data")
public class RequestDataController {

    @Autowired
    private RequestHeaderService requestListService;

    @Autowired
    private RequestDetailsService requestDetailsService;

    @Operation(summary = "Get all requests", description = "Fetches all license approval requests")
    @GetMapping("/getall/requests")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllRequestList() throws IOException {
        long start = System.currentTimeMillis();
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Requests fetched successfully ");
        response.setData(requestListService.totalRequest());
        long end = System.currentTimeMillis();
        log.info("Upload took: " + (end - start) + " ms");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get course and certificates", description = "Returns course and certificate details by request ID")
    @GetMapping(value = "/getall/certificates/{requestDetailsId}")
    public ResponseEntity<Set<CoursesDTO>> getCourseDetails(
            @Parameter(description = "ID of request details") @PathVariable String requestDetailsId) throws IOException {
        return ResponseEntity.ok(requestDetailsService.getCourseandCertificates(requestDetailsId));
    }

    @Operation(summary = "Get quarterly report", description = "Returns license request count per quarter")
    @GetMapping("/quarterlyReport")
    public ResponseEntity<Map<Month, Integer>> quarterlyReport() {
        return new ResponseEntity<>(requestListService.quarterlyReport(), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get annual report", description = "Returns license request count per month for a year")
    @GetMapping("/annualReport")
    public ResponseEntity<Map<Month, Integer>> annualReport() {
        return new ResponseEntity<>(requestListService.annualReport(), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get requests by employee", description = "Fetches requests made by a specific employee")
    @GetMapping(value = "/getallrequests/{empID}")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllRequestListByEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long empID) throws IOException {
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Requests fetched successfully with employee id : " + empID);
        response.setData(requestListService.totalRequestsByEmp(empID));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get pending licenses", description = "Fetches all pending license requests")
    @GetMapping("/getall/pendingLicense")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllPendingLicenses() throws IOException {
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Requests fetched successfully ");
        response.setData(requestListService.allPendingLicense());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get active licenses", description = "Fetches all currently active license requests")
    @GetMapping("/getall/activeLicense")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllActiveLicenses() throws IOException {
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Requests fetched successfully ");
        response.setData(requestListService.allActiveLicense());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get expiring soon licenses", description = "Fetches all licenses that are expiring soon")
    @GetMapping("/getall/ExpiringSoonLicense")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllExpiringSoonLicense() throws IOException {
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Requests fetched successfully ");
        response.setData(requestListService.allExpireSoonLicense());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get expired licenses", description = "Fetches all expired licenses")
    @GetMapping("/getall/ExpiredLicense")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllExpiredLicense() throws IOException {
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Requests fetched successfully ");
        response.setData(requestListService.allExpiredLicense());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get pending requests", description = "Fetches all license requests with status pending")
    @GetMapping("/getall/pendingRequests")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllPendingRequest() throws IOException {
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Pending Requests fetched successfully ");
        response.setData(requestListService.pendingRequest());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get rejected requests", description = "Fetches all license requests that were rejected")
    @GetMapping("/getall/rejectedRequest")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllRejectedRequest() throws IOException {
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Rejected Requests fetched successfully ");
        response.setData(requestListService.rejectedRequest());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get approved requests", description = "Fetches all license requests that are approved")
    @GetMapping("/getall/approvedRequest")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllApprovedRequest() throws IOException {
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Approved Requests fetched successfully ");
        response.setData(requestListService.approvedRequest());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
    
    @Operation(summary = "Get requests by Business Unit", description = "Fetches all license requests with the business unit")
    @GetMapping("/getall/byBU/{BUname}")
    public ResponseEntity<BaseResponse<ResponseDTO<List<RequestResponseDTO>>>> getAllRequestByBU(@PathVariable String BUname) throws IOException {
        BaseResponse<ResponseDTO<List<RequestResponseDTO>>> response = new BaseResponse<>();
        response.setMessage("All Requets per Business unit "+BUname);
        response.setData(requestListService.totalRequestPerBU(BUname));
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
    
    @Operation(summary = "Get Log Messages", description = "License Log messages per request id ")
    @GetMapping("/LicenseLogs/{requestId}")
    public ResponseEntity<Set<LicenseLogMessages>> licenseLogMessages(@PathVariable String requestId) {
        return new ResponseEntity<>(requestDetailsService.licenseLogPerRequestId(requestId), HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/overallReport")
    public ResponseEntity<Map<Long,List<RequestDetailsDTO>>> overallReport(){
    	return ResponseEntity.ok(requestDetailsService.totalReport());
    }
}
