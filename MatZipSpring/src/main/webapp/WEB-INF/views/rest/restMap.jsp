<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <style>
	    .label {margin-bottom: 96px;}
		.label * {display: inline-block;vertical-align: top;}
		.label .left {background: url("https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_l.png") no-repeat;display: inline-block;height: 24px;overflow: hidden;vertical-align: top;width: 7px;}
		.label .center {background: url(https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_bg.png) repeat-x;display: inline-block;height: 24px;font-size: 12px;line-height: 24px;}
		.label .right {background: url("https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_r.png") -1px 0  no-repeat;display: inline-block;height: 24px;overflow: hidden;width: 6px;}
    </style>
    
<div id="sectionContainerCenter">
	<div id="mapContainer" style="width:100%; height:100%;"></div>
	
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9996836ad8617fab6206b5bcc9625c1f"></script>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script>			
		var markerList = [] // 마커 리스트
	
		const options = { //지도를 생성할 때 필요한 기본 옵션
			center: new kakao.maps.LatLng(35.958437, 128.486084), //지도의 중심좌표.
			level: 5 //지도의 레벨(확대, 축소 정도)
		}
	
		const map = new kakao.maps.Map(mapContainer, options);
		
		function getRestaurantList() {
			// 마커 모두 지우기
			markerList.forEach(function(marker) {
				marker.setMap(null)
			})
			
			const bounds = map.getBounds()
			const southWest = bounds.getSouthWest()
			const northEast = bounds.getNorthEast()
			
			console.log('southWest:' + southWest)
			console.log('northEast:' + northEast)
			
			const sw_lat = southWest.getLat()
			const sw_lng = southWest.getLng()
			const ne_lat = northEast.getLat()
			const ne_lng = northEast.getLng()			
			
			axios.get('/rest/ajaxGetList', {
				params: {
					sw_lat, sw_lng, ne_lat, ne_lng
				}
			}).then(function(res) {
				console.log(res.data)
				
				res.data.forEach(function(item) {					
					createMarker(item)
				})
			})		
		}
		
		// tilesloaded 하면 화면 움직이고 딱 멈췄을때 그때 서버에서 값을 가져옴
		kakao.maps.event.addListener(map, 'tilesloaded', getRestaurantList)   
		
		//마커생성
		function createMarker(item) {			
			var content = document.createElement('div')
			content.className = 'label'
			
			var leftSpan = document.createElement('span')
			leftSpan.className = 'left'
			
			var rightSpan = document.createElement('span')
			rightSpan.className = 'right'
			
			var centerSpan = document.createElement('span')
			centerSpan.className = 'center'
			centerSpan.innerText = item.nm
			
			// 지도에 좋아요 눌렀을시 하트 추가하는 코드 
			var restNm = item.nm
			if(item.is_favorite == 1) {
				restNm += ' ♥'	
			}
			
			centerSpan.innerText = restNm
						
			content.appendChild(leftSpan)
			content.appendChild(centerSpan)
			content.appendChild(rightSpan)			
			//var content = `<div class ="label"><span class="left"></span><span class="center">\${item.nm}</span><span class="right"></span></div>`
			var mPos = new kakao.maps.LatLng(item.lat, item.lng)			
			var marker = new kakao.maps.CustomOverlay({
			    position: mPos,
			    content: content
			});			
			addEvent(content, 'click', function() {
				console.log('마커 클릭: ' + item.i_rest)
				moveToDetail(item.i_rest)
			})
			
			marker.setMap(map)
			
			markerList.push(marker)
		}
		
		// 지도에 가게누르면 rest/detail.jsp 로 가게끔 해놨음
		function moveToDetail(i_rest) {
			location.href = '/rest/detail?i_rest=' + i_rest
		}
		
		
		function addEvent(target, type, callback) {
			 if (target.addEventListener) {
			    target.addEventListener(type, callback);
		    } else {
		        target.attachEvent('on' + type, callback);
		    }
		}
		
		
		// check for Geolocation support
		if (navigator.geolocation) {
		  console.log('Geolocation is supported!');
		  
		  var startPos;		  
		  navigator.geolocation.getCurrentPosition(function(pos) {		
			  	startPos = pos			  
			    console.log('lat : ' + startPos.coords.latitude)
			    console.log('lng : ' + startPos.coords.longitude)
			    
			    if(map) {
				    var moveLatLon = new kakao.maps.LatLng(startPos.coords.latitude, startPos.coords.longitude)
				    map.panTo(moveLatLon)			    	
			    }
		  });
		  
		} else {
		  console.log('Geolocation is not supported for this Browser/OS.');
		}
	
	</script>
</div>