<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>${detail.title}</title>
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
		.sn-content pre {
			white-space: pre-wrap;
		}
	</style>

</head>

<body>

	<!-- Brand Start -->
	<div class="brand">
		<div class="container">
			<div class="row align-items-center">
				<div class="col-lg-3 col-md-4">
					<div class="b-logo">
						<a href="index.jsp"> <img src="img/logo.png" alt="Logo">
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
					</div>
				</div>
			</nav>
		</div>
	</div>
	<!-- Nav Bar End -->

	<!-- Breadcrumb Start -->
	<div class="breadcrumb-wrap">
		<div class="container">
			<ul class="breadcrumb">
				<li class="breadcrumb-item"><a href="/Home">Trang chủ</a></li>
				<li class="breadcrumb-item"><a href="/Home">Tin tức</a></li>
				<li class="breadcrumb-item active">${detail.category}</li>
			</ul>
			<p class="col-lg-10" style="text-align: right;">
				<fmt:formatDate value="${detail.date}" pattern="dd/MM/yyyy" var="formattedDate" />${formattedDate}</p>
		</div>
	</div>
	<!-- Breadcrumb End -->

	<!-- Single News Start-->
	<div class="single-news">
		<div class="container">
			<div class="row">
				<div class="col-lg-10">
					<div class="sn-container">
						<div class="sn-img">
							<img src="${detail.image}" />
						</div>
						<div class="sn-content">
							<h1 class="sn-title">${detail.title}</h1>
							<pre>${detail.content}</pre>
						</div>
						<div class="sn-content">
							<h5 style="text-align: right;"><b>${detail.authorName}</b></h5>
						</div>
					</div>
				</div>

				<div class="col-lg-2">
					<div class="sidebar">
						<div class="sidebar-widget">
							<h2 class="sw-title">Tags</h2>
							<div class="tags">
								<a href="">${detail.tags}</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Single News End-->

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
