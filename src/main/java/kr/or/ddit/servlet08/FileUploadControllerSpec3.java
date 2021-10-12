package kr.or.ddit.servlet08;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.reslovers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.mvc.fileupload.MultipartFile;
import kr.or.ddit.mvc.fileupload.StandardMultipartFile;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FileUploadControllerSpec3{
	
	String saveFolderURL = "/resources/images";

	@RequestMapping(value = "/fileUploadSpec3.do", method = RequestMethod.POST)
	public String upload(HttpServletRequest req, HttpServletResponse resp, HttpSession session,
//						MultipartFile filePart,
						@RequestParam("uploader") String uploader) throws ServletException, IOException {
		
		session.setAttribute("uploader", uploader);
		
//		Body -> N 개의 파트 (2.5 FileItem, 3.0 이후부터 Part)
		
		MultipartFile filePart = new StandardMultipartFile( req.getPart("uploadFile"));
//		log.info("upload file : {}",filePart);
		
//	    filePart.getName(); // Part Name 을 가져옴
		
		String originalFileName = filePart.getOriginalFilename();
		log.info("원본 파일명 : {}",originalFileName);
		
		String contentType = filePart.getContentType();
		if(contentType != null) { // 정상적인 경우
			log.info("허용여부 : {}", contentType.startsWith("image/"));	
		}
		
		log.info("파일 크기 : {}", filePart.getSize());
		if(!filePart.isEmpty()) { // 정상적인 경우
			File saveFolder = new File(req.getServletContext().getRealPath(saveFolderURL)); // 저장 경로 지정
			String saveName = UUID.randomUUID().toString(); // 중복 없는 랜덤 아이디값 생성
			
			// 안정성을 위해서 이름을 새로 지정
			File saveFile = new File(saveFolder, saveName);
			
			// 파일 저장 경로 지정
			String fileURL = saveFolderURL + "/" + saveName;
			filePart.transferTo(saveFile);
			session.setAttribute("uploadFile", fileURL);
		}
		return "redirect:11/fileUploadForm.jsp";
	}
}	

//	private String parseDisposition(Part filePart) {
//		
////		Content-Disposition: form-data; name="uploadFile"; filename="test.jpg"
//		
//		String headerValue = filePart.getHeader("Content-Disposition"); // 헤더의 이름 추출
//		
//		int fromIndex = headerValue.indexOf("filename"); // 헤더의 이름들 중 filename이 가지고있는 인덱스 추출
//		
//		String filename = null;
//		
//		if(fromIndex >= 0) { // filename 이 있는 경우
//			int index = headerValue.indexOf("=", fromIndex); // fromIndex 의 값부터 = 를 찾아냄
//			// index 의 + 1 자리부터 구하고, " 의 값을 공백으로 바꿈. ;의 값부터 잘라냄
//			filename = headerValue.substring(index + 1).split(";")[0].replaceAll("\"", ""); 
//			// filename="test.jpg" 에서 test.jpg 만 추출
//		}else { // 없는 경우
//			throw new IllegalArgumentException("일반 파라미터에 해당하는 파트는 파싱할 수 없음.");
//		}
//		
//		return filename;
//	}

