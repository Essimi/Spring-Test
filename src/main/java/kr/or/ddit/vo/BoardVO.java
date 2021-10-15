package kr.or.ddit.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import kr.or.ddit.mvc.fileupload.MultipartFile;
import kr.or.ddit.validate.groups.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(of = "boNo")
@ToString(exclude = {"replyList", "attatchList"}) // 해당값 toString 에서 제외
public class BoardVO implements Serializable{
	
	int rnum;
	
	@NotNull(groups = UpdateGroup.class)
	private Integer boNo;
	@NotBlank
	private String boTitle;
	@NotBlank
	private String boWriter;
	@NotBlank
	private String boIp;
	@Email
	private String boMail;
	@NotBlank
	private String boPass;
	private String boContent;
	private String boDate;
	private Integer boRep;
	private Integer boHit;
	private Integer boRec;
	private Integer boParent;
	
	private List<ReplyVO> replyList;
	
	private MultipartFile[] boFiles;
	
	public void setBoFiles(MultipartFile[] boFiles) {
		
		if(boFiles == null) return;
		
		this.boFiles = boFiles;
		this.attatchList = new ArrayList<>();
		
		for(MultipartFile tmp : boFiles) {
			if(tmp.isEmpty()) continue;
			attatchList.add(new AttatchVO(tmp));
		
		}
		
		log.info("attatchList = {}", attatchList);
	}
	
	

	// 존재하는 첨부파일들을 사용하기 위한 객체
	private List<AttatchVO> attatchList; // 여러개의 첨부파일, DB에서 조회시 사용
//	log.info("attatchList : {}", attatchList);
	
	private int startAttNo; // 첨부파일 개수만큼 번호 지정, xml 에서 사용
	
	// 첨부파일들을 삭제하기 위한 객체, 여러개일 수 있기 때문에 배열로 지정
	private int[] delAttNos;
	
	private int repCnt; 
	private int atchCnt;
	

}
