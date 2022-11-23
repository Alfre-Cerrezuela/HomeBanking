const { createApp } = Vue;

createApp({
    data() {
        return {
            arrayClientes: [],
            cliente: {},
            tarjetas: [],
            id: "",
            id_borrar_tarjeta: "",
            date: new Date(),
            dateActual: "",
            dateFalse: new Date("2027-11-16")
        }
    },

    created() {
        this.getData("/api/clients/current")
    },

    methods: {
        getData(url) {
            axios.get(url).then(response => {
                this.cliente = response.data
                /*  this.cuentas = this.arrayClientes.map(cliente => cliente.account) */
                // let local = JSON.parse(localStorage.getItem("cliente"))
                // if (local) {
                //     this.cliente = local
                //     this.tarjetas = this.cliente.cards
                // }
                console.log(this.cliente);
                console.log(this.tarjetas);
                this.tarjetas = this.cliente.cards
                this.dateActual = this.date.getFullYear() + "-" + (this.date.getMonth() + 1) + "-" + (this.date.getDate() + 1)
            })
        },

        girarTarjeta(tarjeta) {
            this.id = tarjeta.id
            let tarjetaAdelante = document.getElementById("tarjetaAdelante-" + this.id)
            let tarjetaAtras = document.getElementById("tarjetaAtras-" + this.id)
            tarjetaAdelante.classList.toggle("voltear")
            tarjetaAtras.classList.toggle("girar")
            setTimeout(() => {
                tarjetaAdelante.classList.toggle("voltear")
                tarjetaAtras.classList.toggle("girar")
            }, 5000)
        },

        cerrarSesion() {
            Swal.fire({
                title: 'Estás seguro de cerrar la sesión?',
                // text: "que quieres salir?",
                icon: 'warning',
                showCancelButton: true,
                background: "#212529",
                color: "#ffffff",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si!',
                cancelButtonText: 'No!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/logout').then(response => {
                        localStorage.clear()
                        window.location.pathname = "/index.html"
                    })
                }
            })
        },

        mostrarDigitos(tarjeta) {
            let cambiar = document.getElementById(tarjeta.number)
            cambiar.innerHTML = `
            <span class="num4 ocultar">${tarjeta.number.slice(0, 4)}</span>
            <span class="num4 ocultar">${tarjeta.number.slice(5, 9)}</span>
            <span class="num4 ocultar">${tarjeta.number.slice(10, 14)}</span>
            <span class="num4">${tarjeta.number.slice(15, 19)}</span>
            <button v-on:click="girarTarjeta(tarjeta)"
            class="flecha-vuelta-adelante animate__animated animate__slideInLeft">
            <span class="material-symbols-outlined">multiple_stop</span>
            </button>`
        },
        tirarError(titulo, icono, position) {
            const Toast = Swal.mixin({
                toast: true,
                position: position,
                showConfirmButton: false,
                timer: 3500,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                }
            })

            Toast.fire({
                icon: icono,
                title: titulo
            })
        },

        borrarTarjeta() {
            Swal.fire({
                title: 'Estás seguro de borrar la tarjeta?',
                // text: "que quieres salir?",
                icon: 'warning',
                // input: "password",
                showCancelButton: true,
                background: "#212529",
                color: "#ffffff",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si!',
                cancelButtonText: 'No!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch("/api/clients/current/cards/delete", `id=${this.id_borrar_tarjeta}`
                        // & password=melba123
                    )
                        .then(() => {
                            this.tirarError("Se borró la tarjeta correctamente", "success", "top-end")
                            this.getData("/api/clients/current")
                        })
                        .catch(error => { console.log(error); })
                }
            })
        },
        marcarVencimiento() {

        }

    },

    computed: {

    },

}).mount("#app");