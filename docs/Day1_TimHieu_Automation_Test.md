1. Tìm hiểu một số framework Automation Test

1.1. Cucumber using Serenity BDD and Screenplay

Link tài liệu: https://serenity-bdd.github.io/docs/tutorials/cucumber-screenplay

Serenity BDD là gì?

Serenity BDD là một framework hỗ trợ kiểm thử tự động, giúp tổ chức kịch bản test và tạo báo cáo chi tiết sau khi chạy test. Serenity hỗ trợ kiểm thử ứng dụng web và API.

Cucumber là gì?

Cucumber là công cụ hỗ trợ viết kịch bản kiểm thử theo hướng BDD (Behavior-Driven Development). Kịch bản thường được viết bằng ngôn ngữ Gherkin với cấu trúc:

Given: Điều kiện ban đầu.

When: Hành động được thực hiện.

Then: Kết quả mong muốn.

Ví dụ:

Feature: Đăng nhập hệ thống

Scenario: Đăng nhập thành công
Given người dùng đang ở màn hình đăng nhập
When người dùng nhập tài khoản và mật khẩu hợp lệ
And nhấn nút Đăng nhập
Then hệ thống hiển thị màn hình trang chủ

Screenplay là gì?

Screenplay là một mẫu thiết kế (Design Pattern) dùng để tổ chức code Automation Test theo hành động của người dùng. Trong Screenplay, Actor đại diện cho người dùng, thực hiện các Task và Interaction để kiểm tra chức năng hệ thống.

Ưu điểm:

Kịch bản dễ đọc, gần với ngôn ngữ nghiệp vụ.

Dễ tổ chức và tái sử dụng code.

Có thể tạo báo cáo kiểm thử chi tiết với Serenity BDD.

Ứng dụng: Phù hợp với dự án cần kiểm thử nghiệp vụ nhiều bước, có nhiều kịch bản và cần báo cáo kết quả rõ ràng.

1.2. Playwright

Link tài liệu: https://playwright.dev/docs/intro

Playwright là một framework kiểm thử tự động ứng dụng web, hỗ trợ kiểm thử end-to-end (E2E). Playwright cung cấp công cụ chạy test, kiểm tra kết quả, cô lập các bài test và hỗ trợ chạy song song.

Các trình duyệt hỗ trợ:

Chromium.

Firefox.

WebKit.

Ngôn ngữ lập trình: Tài liệu hướng dẫn được cung cấp ở trên tập trung vào Playwright Test với JavaScript/TypeScript. Playwright cũng có các thư viện cho Java, Python và .NET.

Ưu điểm:

Hỗ trợ tự động hóa trình duyệt trên nhiều nền tảng.

Có cơ chế chờ tự động khi thao tác và kiểm tra phần tử.

Hỗ trợ chạy test song song và tạo báo cáo.

Hỗ trợ kiểm thử trên nhiều trình duyệt.

Ứng dụng: Phù hợp với kiểm thử tự động ứng dụng web, kiểm tra các luồng nghiệp vụ từ đầu đến cuối và chạy test trong quy trình CI/CD.

1.3. So sánh Serenity BDD with Cucumber and Screenplay với Playwright

Tiêu chí

Serenity BDD + Cucumber + Screenplay

Playwright

Mục đích

Tổ chức kịch bản BDD, kiểm thử và báo cáo

Tự động hóa kiểm thử web E2E

Ngôn ngữ

Java và Gherkin

JavaScript/TypeScript, Java, Python, .NET

Cách viết test

Cucumber dùng Given/When/Then; Screenplay tổ chức hành động

Viết test bằng code và các lệnh tương tác với trình duyệt

Báo cáo

Serenity tạo báo cáo chi tiết

Playwright Test có báo cáo HTML

Điểm nổi bật

Kịch bản gần với nghiệp vụ, cấu trúc test rõ ràng

Hỗ trợ đa trình duyệt, chờ tự động và chạy song song

Kết luận: Cả hai đều hỗ trợ kiểm thử tự động. Serenity BDD kết hợp Cucumber và Screenplay chú trọng cách mô tả nghiệp vụ, tổ chức kịch bản và báo cáo. Playwright chú trọng khả năng tự động hóa trình duyệt và thực thi kiểm thử web hiệu quả.

2. Trả lời câu hỏi về Automation Test

Câu 1. Automation Test là gì?

Automation Test (kiểm thử tự động) là việc tái hiện các thao tác của người manual test 

Câu 2. Tại sao cần Automation Test?

Automation Test được sử dụng vì: 
1, Nhanh: sẽ chỉ mất thời gian đầu viết code, khi sử dụng sẽ tự động và nhanh hơn
2, Chi phí thấp: tiết kiệm thời gian và nhân lực
3, Độ tin cậy cao: Hệ thống tự động chạy sẽ có độ tin cậy cao hơn
4, Khả năng lặp: có thể lặp đi lặp lại nhiều lần test
5, Khả năng tái sử dụng: Giúp thực hiện các bài test nhiều lần với cùng một quy trình

Câu 3. Khi nào cần chạy kịch bản Automation Test?

Các kịch bản Automation Test thường được chạy trong những trường hợp:

Sau khi có thay đổi code: Kiểm tra các chức năng bị ảnh hưởng bởi thay đổi.

Trước khi phát hành phiên bản mới: Kiểm tra các chức năng quan trọng để hạn chế lỗi khi đưa phần mềm đến người dùng.

Khi thực hiện Regression Test: Đảm bảo các chức năng cũ vẫn hoạt động sau khi sửa bug hoặc thêm tính năng.

Khi chạy Smoke Test: Kiểm tra nhanh các chức năng chính để xác định phiên bản có đủ ổn định cho các bước kiểm thử tiếp theo hay không.

Khi có nhiều dữ liệu hoặc kịch bản lặp lại: Tự động hóa giúp giảm công sức thực hiện thủ công.

Trong quy trình CI/CD: Tự động chạy test khi code được commit, build hoặc triển khai theo cấu hình của dự án.