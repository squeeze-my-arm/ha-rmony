$(document).ready(function() {
    $('.button').on('click', function() {
        var newNickname = $('.edit-input:eq(0)').val();
        var newIntroduction = $('.edit-input:eq(1)').val();

        const userId = document.querySelector(".button").id;
        console.log(userId)
        // 추출한 사용자 ID를 사용하여 정보 업데이트 요청
        $.ajax({
            type: 'PUT',
            url: '/api/users/' + userId,
            contentType: 'application/json',
            data: JSON.stringify({
                nickname: newNickname,
                introduction: newIntroduction
            }),
            success: function(response) {
                console.log('User updated:', response);
            },
            error: function(xhr, status, error) {
                console.error('Update failed:', error);
            }
        });
    });
});
$('.btn').on('click', function() {
    const userId = document.querySelector(".btn").id;
    (async () => {
        const { value: getPassword } =
            await Swal.fire({
                title: '변경할 비밀번호',
                text: 'ㅋㅋ',
                input: 'password',
                inputPlaceholder: '변경할 패스워드 입력..'
            })
        // 이후 처리되는 내용.
        if (getPassword) {
            // 패스워드 변경 처리
            $.ajax({
                type: 'PUT',
                url: '/api/users/' + userId, // 변경된 URL로 수정
                contentType: 'application/json',
                data: JSON.stringify({
                    password: getPassword
                }),
                success: function(response) {
                    console.log(getPassword)
                    console.log('Password updated:', response);
                    alert('비밀번호 변경이 완료되었습니다.');
                },
                error: function(xhr, status, error) {
                    console.error('Password update failed:', error);
                    alert('비밀번호 변경 중 오류가 발생하였습니다. 다시 시도해주세요.');
                }
            });
        }
    })();
});

$('.leave-button').on('click', function() {
    const userId = document.querySelector(".button").id;

    // 사용자 정보 삭제 요청
    $.ajax({
        type: 'DELETE',
        url: '/api/users/' + userId,
        success: function(response) {
            console.log('User deleted:', response);
            // 삭제 성공 시 다음 동작 수행 (예: 리다이렉트 또는 메시지 표시)
            alert('회원 탈퇴가 완료되었습니다.');
            window.location.href = '/'; // 메인 페이지로 리다이렉트
        },
        error: function(xhr, status, error) {
            console.error('Deletion failed:', error);
            alert('회원 탈퇴 중 오류가 발생하였습니다. 다시 시도해주세요.');
        }
    });
});

