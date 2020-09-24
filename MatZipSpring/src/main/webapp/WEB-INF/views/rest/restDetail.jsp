<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div>
	<div class="recMenuContainer">
		<c:forEach items="${recMenuList}" var="item">
			<div class="recMenuItem" id="recMenuItem_${item.seq}">
				<div class="pic">
					<c:if test="${item.menu_pic != null and item.menu_pic != ''}">
						<img src="/res/img/rest/${data.i_rest}/rec_menu/${item.menu_pic}">
					</c:if>
				</div>
				<div class="info">
					<div class="nm">${item.menu_nm}</div>
					<div class="price"><fmt:formatNumber type="number" value="${item.menu_price}"/>원</div>
				</div>
				<c:if test="${loginUser.i_user == data.i_user}">
					<div class="delIconContainer" onclick="delRecMenu(${item.seq})">
						<span class="material-icons">clear</span>
					</div>
				</c:if>
			</div>
		</c:forEach>
	</div>
	<div id="sectionContainerCenter">
		<div>
			<c:if test="${loginUser.i_user == data.i_user}">
				<button onclick="isDel()">가게 삭제</button>
				
				<h2>- 추천 메뉴 -</h2>
				<div>
					<!--     					onclick이부분이 밑에 <script> 가 실행되어  스크립트로 인한 여러소스들이 추가된것 -->
					<div><button type="button" onclick="addRecMenu()">추천 메뉴 추가</button></div>
					<form id="recFrm" action="/rest/recMenus" enctype="multipart/form-data" method="post">
						<input type="hidden" name="i_rest" value="${data.i_rest}">
						<div id="recItem"></div>
						<div><input type="submit" value="등록"></div>
					</form>
				</div>
				
				<h2>- 메뉴 -</h2>
				<div>
					<form id="menuFrm" action="/rest/menus" enctype="multipart/form-data" method="post">
						<!-- i_rest, menu_pic 은 즉 RestFile.java 에 넣을것 -->
						<input type="hidden" name="i_rest" value="${data.i_rest}">
						<input type="file" name="menu_pic" multiple>						
						<div><input type="submit" value="등록"></div>
					</form>
				</div>
			</c:if>
			
			<div class="restaurant-detail">
				<div id="detail-header">
					<div class="restaurant_title_wrap">
						<span class="title">
							<h1 class="restaurant_name">${data.nm}</h1>						
						</span>
					</div>
					<div class="status branch_none">
						<span class="cnt hit">${data.hits}</span>					
						<span class="cnt favorite">${data.cd_category_nm}</span>
					</div>
				</div>
				<div>
					<table>
						<caption>레스토랑 상세 정보</caption>
						<tbody>
							<tr>
								<th>주소</th>
								<td>${data.addr}</td>
							</tr>
							<tr>
								<th>카테고리</th>
								<td>${data.cd_category_nm}</td>
							</tr>
							<tr>
								<th>메뉴</th>
								<td>
									<div class="menuList">
										<!--3초과하면   0,1,2  3번 돌고 나머지사진은 밑에서 몇장이상 남았는지 잔여물로 남음   -->
										<c:if test="${fn:length(menuList) > 0 }">
											<c:forEach var="i" begin="0" end="${fn:length(menuList) > 3 ? 2 : fn:length(menuList) - 1}">
												<div class="menuItem">
													<img src="/res/img/rest/${data.i_rest}/menu/${menuList[i].menu_pic}">
													<c:if test="${loginUser.i_user == data.i_user }">
														<div class="delIconContainer" onclick="delMenu(${menuList[i].seq})">
															<span class="material-icons">clear</span>
														</div>
													</c:if>
												</div>												
											</c:forEach>
										</c:if>
										<!-- 사진 총 몇장이상 있다라고 표시하는 구간 -->
										<c:if test="${fn:length(menuList) > 3}">
											<div class="menuItem bg_black">
												<div class="moreCnt">
													+${fn:length(menuList) - 3}
												</div>
											</div>
										</c:if>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script>
	function delRecMenu(seq) {
		if(!confirm('삭제하시겠습니까?')) {
			return
		}	
		console.log('seq : ' + seq)
		
		axios.get('/rest/ajaxDelRecMenu', {
			params: {
				i_rest: ${data.i_rest},
				seq: seq
				
			}
		}).then(function(res) {
			console.log(res)
			if(res.data == 1) {
				//엘리먼트 삭제
				var ele = document.querySelector('#recMenuItem_' + seq)
				ele.remove()
			}
		})
	}
	
	var idx = 0;	// 이름중복방지 (후위증감) 변수1,2,3...
	
	// html단에서 <form>으로 만들지않고 스크립트로 함수를 만든이유는 계속 값이 추가되어야하기떄문에.. (유동적으로 하기위해)
	function addRecMenu() {
		var div = document.createElement('div')
		div.setAttribute('id', 'recMenu_' + idx++)
		
		// 메뉴 이름
		var inputNm = document.createElement('input')
		inputNm.setAttribute('type', 'text')
		inputNm.setAttribute('name', 'menu_nm')
		
		// 메뉴가격
		var inputPrice = document.createElement('input')
		inputPrice.setAttribute('type', 'number')
		inputPrice.setAttribute('name', 'menu_price')
		inputPrice.value = '0'
		
		// 메뉴 파일(사진)
		var inputPic = document.createElement('input')
		inputPic.setAttribute('type', 'file')
		inputPic.setAttribute('name', 'menu_pic')
		
		// 삭제
		var delBtn = document.createElement('input') // input으로 한 이유는 value 'x' 값을 주기위해...
		delBtn.setAttribute('type', 'button')
		delBtn.setAttribute('value', 'x')
		delBtn.addEventListener('click', function() {
			div.remove()
		})
		
		div.append('메뉴: ') // 위에 지정한 var div 를 실제 추가하는 속성
		div.append(inputNm)
		div.append(' 가격: ')
		div.append(inputPrice)
		div.append(' 사진: ')
		div.append(inputPic)		
		div.append(delBtn)	// 밑에 addRecMenu() 랑 같음  (뱃속에만들어놓고 바깥에 안꺼내놓으면 의미없는것처럼)
		
		recItem.append(div)
	}
	
	addRecMenu() // 실제 위에 작성한 함수를 바깥세상에 꺼내는 것 ( 애기가 엄마뱃속에서 만들어지면뭐함 ? 바깥에 직접 나와야지 )
	
	function isDel() {
		if(confirm('삭제 하시겠습니까?')) {
			location.href = '/rest/del?i_rest=${data.i_rest}'
		}
	}
</script>