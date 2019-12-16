<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
        <a class="navbar-brand" href="/home"><img src="/images/logo.png" alt="logo"></a>
    </div>
    <!-- Top Menu Items -->
    <ul class="nav navbar-right top-nav">
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> Bienvenido, <span style="color: #3d8b3d; font-family: cursive">${username}</span> </b> <b class="caret"></b></a>
            <ul class="dropdown-menu">
                <#---->
                <#--<li>-->
                    <#--<a href="#"><i class="fa fa-fw fa-user"></i> Profile</a>-->
                <#--</li>-->
                <#--<li>-->
                    <#--<a href="#"><i class="fa fa-fw fa-envelope"></i> Inbox</a>-->
                <#--</li>-->
                <#--<li>-->
                    <#--<a href="#"><i class="fa fa-fw fa-gear"></i> Settings</a>-->
                <#--</li>-->
                <li class="divider"></li>
                <li>
                    <a href="/logout"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                </li>
            </ul>
        </li>
    </ul>
    <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav side-nav">
            <li class="active">
                <a href="/home"><i class="fa fa-fw fa-dashboard"></i>Inicio</a>
            </li>
            <li>
                <a href="/zonaAdmin/actividad"><i class="fa fa-fw fa-bus"></i> Actividad</a>
            </li>

            <li>
                <a href="/rating/verComentarios/"><i class="fa fa-fw fa-comments"></i>Ver comentarios</a>
            </li>

            <li>
                <a href="javascript:;" data-toggle="collapse" data-target="#ruta"><i class="fa fa-fw fa-arrows-v"></i> Ruta <i class="fa fa-fw fa-caret-down"></i></a>
                <ul id="ruta" class="collapse">
                    <li>
                        <a href="/ruta/crear">Agregar Nueva Ruta</a>
                    </li>
                    <li>
                        <a href="/ruta/">Ver Ruta</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="javascript:;" data-toggle="collapse" data-target="#autobus"><i class="fa fa-fw fa-arrows-v"></i> Autobus <i class="fa fa-fw fa-caret-down"></i></a>
                <ul id="autobus" class="collapse">
                    <li>
                        <a href="/autobus/crear">Agregar Nuevo Autobus</a>
                    </li>
                    <li>
                        <a href="/autobus/">Ver Autobus</a>
                    </li>
                    <li>
                        <a href="/autobus/sinRuta">Autobus sin Ruta</a>
                    </li>
                </ul>
            </li>
            <#if usuario??>
                <#list usuario.roles as rol>
                    <#if rol.rol=="ROLE_ADMIN">
                        <li>
                            <a href="javascript:;" data-toggle="collapse" data-target="#demo"><i class="fa fa-fw fa-arrows-v"></i> Usuarios <i class="fa fa-fw fa-caret-down"></i></a>
                            <ul id="demo" class="collapse">
                                <li>
                                    <a href="/zonaAdmin/registrar">Agregar Nuevo Usuario</a>
                                </li>
                                <li>
                                    <a href="/zonaAdmin/usuarios">Ver Usuarios</a>
                                </li>
                            </ul>
                        </li>
                    </#if>
                </#list>
                </#if>


        </ul>
    </div>
    <!-- /.navbar-collapse -->
</nav>
