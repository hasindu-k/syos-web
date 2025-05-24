<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome to SYOS</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;800&display=swap" rel="stylesheet">
     <link
    rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    integrity="sha512-MztYpBKPZl8gSKy8Xl4bfxc3Krkt3z4vcqEzq8m9jc+iFDJIlA46+PfPMYcixjxgLJQ0C1Ev3Y9DdXvCuVxXOw=="
    crossorigin="anonymous"
    referrerpolicy="no-referrer"
  />
    
    <style>
        body {
            margin: 0;
            padding: 0;
            background: #f4f4f4;
            font-family: 'Poppins', sans-serif;
        }
        .main-container {
            width: 1000px;
            height: 620px;
            margin: 60px auto;
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 4px 28px rgba(0,0,0,0.09);
            display: flex;
            overflow: hidden;
        }
        .left-section {
            flex: 1;
            background: #1976d2;
            color: #fff;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 32px 18px;
            position: relative;
            text-align: center;
        }
        .shopping-icon {
            font-size: 50px;
            margin-bottom: 24px;
            color: #fff;
        }
        .welcome-title {
            font-size: 36px;
            font-weight: 800;
            margin-bottom: 20px;
            text-shadow: 1px 1px 4px rgba(0,0,0,0.2);
        }
        .welcome-message {
            font-size: 18px;
            line-height: 1.8;
            max-width: 420px;
            font-weight: 400;
            opacity: 0.95;
        }
        .logo-box {
            border: 2px solid #fff;
            border-radius: 8px;
            padding: 18px 38px;
            margin-bottom: 34px;
            background: rgba(255,255,255,0.07);
        }
        .logo-img {
            width: 250px;
            height: auto;
            display: block;
            margin: 0 auto;
        }

        .right-section {
            flex: 1;
            background: #fff;
            color: #222;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 32px 18px;
            border-left: 1.5px solid #e0e0e0;
        }
        .choose-title {
            font-size: 22px;
            margin-bottom: 32px;
            text-align: center;
            color: #222;
            font-weight: 750;
        }
        .button-group {
            display: flex;
            flex-direction: column;
            gap: 18px;
        }
        .btn {
            min-width: 180px;
            padding: 12px 0;
            font-size: 20px;
            color: #1976d2;
            background: #fff;
            border: 2px solid #1976d2;
            border-radius: 20px;
            text-decoration: none;
            text-align: center;
            font-weight: bold;
            transition: background 0.2s, color 0.2s;
        }
        .btn:hover {
            background: #1976d2;
            color: #fff;
        }
        @media (max-width: 800px) {
            .main-container {
                flex-direction: column;
                width: 98vw;
                height: auto;
                min-height: 600px;
            }
            .left-section, .right-section {
                border-left: none;
                border-top: 1.5px solid #e0e0e0;
            }
        }
    </style>
</head>
<body>
    <div class="main-container">
        <div class="left-section">
            <i class="fas fa-shopping-cart shopping-icon"></i>
            <div class="welcome-title">Welcome to SYOS.</div>
            <div class="welcome-message">
                Your one-stop destination for unbeatable deals and everyday essentials.<br>
                Start saving. Start shopping. Start with <strong>SYOS</strong>.
            </div>
        </div>
        
        <div class="right-section">
            <div class="logo-box">
                <img src="<%=request.getContextPath()%>/img/SYOS.png" alt="SYOS Logo" class="logo-img" />
            </div>
            <div class="choose-title">Choose an option below.</div>
            <div class="button-group">
                <a href="register" class="btn">Sign up</a>
                <a href="login" class="btn">Login</a>
            </div>
        </div>
    </div>
</body>
</html>
