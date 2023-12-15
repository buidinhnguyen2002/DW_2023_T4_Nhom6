<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Trang chủ</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport">
<meta content="Bootstrap News Template - Free HTML Templates"
	name="keywords">
<meta content="Bootstrap News Template - Free HTML Templates"
	name="description">

<!-- Favicon -->
<link href="img/favicon.ico" rel="icon">

<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css?family=Montserrat:400,600&display=swap"
	rel="stylesheet">

<!-- CSS Libraries -->
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css"
	rel="stylesheet">
<link href="lib/slick/slick.css" rel="stylesheet">
<link href="lib/slick/slick-theme.css" rel="stylesheet">

<!-- Template Stylesheet -->
<link href="css/style.css" rel="stylesheet">
	<style>
	</style>

</head>

<body>

	<!-- Brand Start -->
	<div class="brand">
		<div class="container">
			<div class="row align-items-center">
				<div class="col-lg-3 col-md-4">
					<div class="b-logo">
						<a href="/Home"> <img src="img/logo.png" alt="Logo">
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-4">
					<div class="b-search">
						<input type="text" placeholder="Tìm kiếm">
						<button>
							<i class="fa fa-search"></i>
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Brand End -->

	<!-- Nav Bar Start -->
	<div class="nav-bar">
		<div class="container">
			<nav class="navbar navbar-expand-md bg-dark navbar-dark">
				<a href="#" class="navbar-brand">MENU</a>
				<button type="button" class="navbar-toggler" data-toggle="collapse"
					data-target="#navbarCollapse">
					<span class="navbar-toggler-icon"></span>
				</button>

				<div class="collapse navbar-collapse justify-content-between"
					id="navbarCollapse">
					<div class="navbar-nav mr-auto">
						<a href="/Home" class="nav-item nav-link active">Trang chủ</a>
						<c:set var="displayedDates" value="" />
					</div>
				</div>
			</nav>
		</div>
	</div>

	<div class="cat-news">
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<h2></h2>
					<div class="row cn-slider">
						<c:forEach items="${list}" var="news">
						<div class="col-md-6">
							<a href="/DetailNews?id=${news.id}">
							<div class="cn-img">
								<div class="cn-title">
										${news.title}
								</div>
								<img src="${news.image}" style="text-align: left;" />
							</div>
								<div class="cn-content">
									<div class="cn-description ">${news.description}</div>
								</div>
							</a>
						</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Footer Start -->
	<div class="footer">
		<div class="container">
			<div class="row">
				<div class="col-lg-3 col-md-6">
					<div class="footer-widget">
						<h3 class="title">Liên hệ</h3>
						<div class="contact-info">
							<p>
								<i class="fa fa-map-marker"></i>Linh Trung, Thủ Đức, TP.HCM
							</p>
							<p>
								<i class="fa fa-envelope"></i>nhom6@gmail.com
							</p>
							<p>
								<i class="fa fa-phone"></i>+123-456-7890
							</p>
							<div class="social">
								<a href=""><i class="fab fa-twitter"></i></a> <a href=""><i
									class="fab fa-facebook-f"></i></a> <a href=""><i
									class="fab fa-linkedin-in"></i></a> <a href=""><i
									class="fab fa-instagram"></i></a> <a href=""><i
									class="fab fa-youtube"></i></a>
							</div>
						</div>
					</div>
				</div>

				<div class="col-lg-3 col-md-6">
					<div class="footer-widget">
						<h3 class="title">Newsletter</h3>
						<div class="newsletter">
							<p>Hãy để lại email để nhận được thông báo sớm nhất khi có tin tức mới</p>
							<form>
								<input class="form-control" type="email"
									placeholder="Email của bạn">
								<button class="btn">Gửi</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Footer End -->

	<!-- Back to Top -->
	<a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>

	<!-- JavaScript Libraries -->
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.bundle.min.js"></script>
	<script src="lib/easing/easing.min.js"></script>
	<script src="lib/slick/slick.min.js"></script>

	<!-- Template Javascript -->
	<script src="js/main.js"></script>
</body>
</html>
