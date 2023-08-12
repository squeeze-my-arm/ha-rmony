$(document).ready(function() {
    $('.save').on('click', function() {
        var newNickname = $('.edit-input:eq(0)').val();
        var newIntroduction = $('.edit-input:eq(1)').val();

        const userId = document.querySelector(".save").id;
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
                const Toast = Swal.mixin({
                    toast: true,
                    position: 'center-center',
                    showConfirmButton: false,
                    timer: 1000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer)
                        toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                });
                Toast.fire({
                    icon: 'success',
                    title: '수정 완료',
                    text: '회원 정보가 수정되었습니다',
                }).then(() => {
                    window.location.reload();
                });
            },
            error: function(xhr, status, error) {
                console.error('Update failed:', error);
            }
        });
    });
});
$('.update').on('click', function() {
    const userId = document.querySelector(".update").id;
    (async () => {
        const { value: getPassword } =
            await Swal.fire({
                title: '비밀번호 변경',
                text: '비밀번호를 입력하세요',
                input: 'password',
                inputPlaceholder: '변경할 비밀번호 입력..'
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
                    Swal.fire({
                        icon: 'success',
                        title: '비밀번호 변경이 완료되었습니다.'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            deleteJwtCookie();
                            window.location.href = "/";
                        }
                    });
                },
                error: function(xhr, status, error) {
                    console.error('Password update failed:', error);
                    Swal.fire({
                        icon: 'error',
                        title: '비밀번호 변경 중 오류가 발생하였습니다.'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.reload();
                        }
                    });
                }
            });
        }
    })();
});

$('.leave-button').on('click', function() {
    const userId = document.querySelector(".leave-button").id;

    Swal.fire({
        title: '정말 탈퇴하시겠습니까 ?',
        showCancelButton: true,
        confirmButtonText: '탈퇴하기',
        cancelButtonText: '취소하기',
        preConfirm: (username) => {
            return new Promise((resolve, reject) => {
                // 사용자 정보 삭제 요청
                $.ajax({
                    type: 'DELETE',
                    url: '/api/users/' + userId,
                    success: function(response) {
                        console.log('User deleted:', response);
                        // 삭제 성공 시 다음 동작 수행 (예: 리다이렉트 또는 메시지 표시)
                        const Toast = Swal.mixin({
                            toast: true,
                            position: 'center-center',
                            showConfirmButton: false,
                            timer: 1000,
                            timerProgressBar: true,
                            didOpen: (toast) => {
                                toast.addEventListener('mouseenter', Swal.stopTimer)
                                toast.addEventListener('mouseleave', Swal.resumeTimer)
                            }
                        });
                        Toast.fire({
                            icon: 'success',
                            title: '안녕히가세요',
                            text: '그동안 이용해주셔서 감사합니다:)',
                        }).then(() => {
                            deleteJwtCookie();
                            window.location.href = '/';
                        });
                        resolve();
                    },
                    error: function(xhr, status, error) {
                        console.error('Deletion failed:', error);
                        const Toast = Swal.mixin({
                            toast: true,
                            position: 'center-center',
                            showConfirmButton: false,
                            timer: 1000,
                            timerProgressBar: true,
                            didOpen: (toast) => {
                                toast.addEventListener('mouseenter', Swal.stopTimer)
                                toast.addEventListener('mouseleave', Swal.resumeTimer)
                            }
                        });
                        Toast.fire({
                            icon: 'error',
                            title: '탈퇴 실패',
                            text: xhr.responseText,
                        });
                        reject();
                    }
                });
            });
        },
        allowOutsideClick: () => !Swal.isLoading() // 모달 바깥 클릭 시 닫히지 않도록 설정
    });

});

function logout() {
    deleteJwtCookie();
    Swal.fire({
        icon: 'success',
        title: '로그아웃이 완료되었습니다'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = "/";
        }
    });
}

function deleteJwtCookie() {
    const cookieName = 'Authorization'; // JWT가 저장된 쿠키의 이름
    // 만료일을 예전 날짜로 설정하여 쿠키를 삭제합니다.
    document.cookie = `${cookieName}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
}