const { createApp } = Vue;

createApp({
    data() {
        return {
            id: new URLSearchParams(location.search).get("id"),
            cuentaDetail: {},
            transaccionesDeCuenta: [],
        }
    },
    mounted() { /* es cuando se creo la parte visual cuando este renderizado */

    },

    created() {
        this.loadData("/api/account/" + this.id)
    },

    methods: {
        loadData(url) {
            axios.get(url).then(response => {

                this.cuentaDetail = response.data
                this.transaccionesDeCuenta = this.cuentaDetail.transaction
                console.log(this.cuentaDetail);
                console.log(this.transaccionesDeCuenta);
            })
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
                    axios.post('/api/logout').then(() => {
                        window.location.pathname = "/index.html"
                    })
                }
            })
        },
    },

    computed: {

    },

}).mount("#app");