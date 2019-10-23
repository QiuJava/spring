<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <ul side-navigation class="nav metismenu" id="side-menu">
            <li class="nav-header">
                <div class="profile-element" uib-dropdown>
                    <img alt="image" class="img-circle" src="img/profile_small.jpg"/>
                    <a uib-dropdown-toggle href>
                            <span class="clear">
                                <span class="block m-t-xs">
                                    <strong class="font-bold" ng-bind="username"></strong>
                             	</span>
                             	<span class="text-muted text-xs block" ng-bind="entityName"></span>
                            </span>
                    </a>
                    <%--<ul uib-dropdown-menu class="animated fadeInRight m-t-xs">--%>
                        <%--<li><a ui-sref="app.profile">Profile</a></li>--%>
                        <%--<li><a ui-sref="app.contacts">Contacts</a></li>--%>
                        <%--<li><a ui-sref="mailbox.inbox">Mailbox</a></li>--%>
                        <%--<li class="divider"></li>--%>
                        <%--<li><a ng-click="logout()">Logout</a></li>--%>
                    <%--</ul>--%>
                </div>
                <div class="logo-element">
                   	 菜单
                </div>
            </li>
            <%  
            /* 
            
            <li ng-class="{active: $state.includes('service')}">
                <a href=""><i class="fa fa-th-large"></i> <span class="nav-label">主页</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('service')}">
                    <li ui-sref-active="active"><a ui-sref="service.addService">欢迎主页</a></li>
                </ul>
            </li>
            
              
           	<!-- 商户管理  -->
            <li ng-class="{active: $state.includes('merchant')}">
                <a href=""><i class="fa fa-th-large"></i> <span class="nav-label">商户管理</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('merchant')}">
                 	<li ui-sref-active="active"><a ui-sref="merchant.addMer">商户进件</a></li>
                    <li ui-sref-active="active"><a ui-sref="merchant.queryMer">查询商户</a></li>
                    <li ui-sref-active="active"><a ui-sref="merchant.auditMer">商户审核</a></li>
                </ul>
            </li>
            
            <!-- 下级代理商管理  -->
           	<li ng-class="{active: $state.includes('agent')}">
                <a href=""><i class="fa fa-th-large"></i> <span class="nav-label">下级代理商管理</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('agent')}">
                    <li ui-sref-active="active"><a ui-sref="agent.addAgent">下级代理商增加</a></li>
                    <li ui-sref-active="active"><a ui-sref="agent.queryAgent">下级代理商管理</a></li>
                </ul>
            </li>
            
            <!-- 机具管理 -->
            <li ui-sref-active="active">
                <a ui-sref="terminal"><i class="fa fa-pie-chart"></i> <span class="nav-label">机具管理</span> </a>
            </li>
            
            <!-- 我的信息  -->
            <li ng-class="{active: $state.includes('myInfo')}">
                <a href=""><i class="fa fa-th-large"></i> <span class="nav-label">我的信息</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('myInfo')}">
                 	<li ui-sref-active="active"><a ui-sref="myInfo.info">我的信息</a></li>
                    <li ui-sref-active="active"><a ui-sref="myInfo.account">我的账户</a></li>
                </ul>
            </li>
            
            <!-- 更多内容  -->
            <li ng-class="{active: $state.includes('noticeInfo')}">
                <a href=""><i class="fa fa-th-large"></i> <span class="nav-label">更多内容</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('noticeInfo')}">
                    <li ui-sref-active="active"><a ui-sref="more.receivedNotices">收到的公告</a></li>
                    <li ui-sref-active="active"><a ui-sref="more.addNotice">新增下发公告</a></li>
                    <li ui-sref-active="active"><a ui-sref="more.sentNotices">下发公告查询</a></li>
                    <li ui-sref-active="active"><a ui-sref="more.addBanner">新增代理商APP banner</a></li>
                    <li ui-sref-active="active"><a ui-sref="more.queryBanners">代理商APP banner查询</a></li>
                </ul>
            </li>
            
           <!-- 系统管理  -->
            <li ng-class="{active: $state.includes('sys')}">
                <a href=""><i class="fa fa-th-large"></i> <span class="nav-label">系统管理</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('sys')}">
                    <li ui-sref-active="active"><a ui-sref="sys.sysMenu">系统菜单</a></li>
                    <li ui-sref-active="active"><a ui-sref="sys.sysUser">系统用户</a></li>
                    <li ui-sref-active="active"><a ui-sref="sys.sysDict">数据字典</a></li>
                </ul>
            </li>
            
            <li>-----------------</li>
            */
            %>       
            
            
            <c:forEach items="${menus}" var="menu">
            <li ng-class="{active: $state.includes('${menu.menuCode }')}">
            	<c:choose>
            	<c:when test="${menu.children != null && menu.children.size() > 0 }">
            	<a href=""><i class="fa fa-th-large ${menu.icons }"></i> <span class="nav-label">${menu.menuName } </span><span class="fa arrow"></span></a>
            	<ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('${menu.menuCode }')}">
            		<c:forEach items="${menu.children }" var="menu1">
            		<li ui-sref-active="active"><a ui-sref="${menu1.menuCode }">${menu1.menuName }</a></li>
            		</c:forEach>
            	</ul>
            	</c:when>
            	<c:otherwise>
            	<a ui-sref="${menu.menuCode }"><i class="fa fa-th-large ${menu.icons }"></i> <span class="nav-label">${menu.menuName }</span> </a>
            	</c:otherwise>
            	</c:choose>
            </li>
            </c:forEach>
            
        </ul>
    </div>
</nav>