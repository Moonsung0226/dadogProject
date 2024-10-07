let map;

// Kakao 지도 초기화
function initMap() {
    const mapContainer = document.getElementById('map');
    const mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.978), // 서울을 기본 중심으로 설정
        level: 3 // 줌 레벨
    };
    map = new kakao.maps.Map(mapContainer, mapOption);
}

// 주소를 좌표로 변환하여 Kakao 지도에 표시
function searchAddressToCoordinates(address) {
    const geocoder = new kakao.maps.services.Geocoder();

    geocoder.addressSearch(address, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
            const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            map.setCenter(coords);
            const marker = new kakao.maps.Marker({
                position: coords,
            });
            marker.setMap(map);
        } else {
            alert('주소를 찾을 수 없습니다.');
        }
    });
}

// DOMContentLoaded 이벤트가 발생하면 보호소 목록을 로드
document.addEventListener("DOMContentLoaded", function() {
    fetch('/api/shelters') // 보호소 정보를 가져오는 API 엔드포인트
        .then(response => response.json())
        .then(shelters => {
            const shelterList = document.getElementById('shelterList');
            shelters.forEach(shelter => {
                const li = document.createElement('li');
                li.textContent = `${shelter.shelNm} - ${shelter.shelAddr} (${shelter.shelCity})`;
                li.style.cursor = 'pointer';
                li.onclick = function() {
                    searchAddressToCoordinates(shelter.shelAddr); // 클릭 시 주소 검색
                };
                shelterList.appendChild(li);
            });
        })
        .catch(error => console.error('보호소 정보를 불러오는 중 오류 발생:', error));

    initMap(); // Kakao 지도 초기화
});
