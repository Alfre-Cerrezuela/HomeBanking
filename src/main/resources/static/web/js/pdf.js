const { createApp } = Vue;

createApp({
    data() {
        return {
            cliente: {},
            cuentas: [],
            num_cuenta: "",
            desde: "",
        }
    },

    created() {
        this.getData("/api/clients/current")
    },

    methods: {
        getData(url) {
            axios.get(url).then(response => {
                this.cliente = response.data
                this.cuentas = this.cliente.accounts
            })
        },
        descargarPDF() {
            if (this.num_cuenta != "" && this.desde != "") {
                axios.post("/api/pdf", `accountNumber=${this.num_cuenta}&fromString=${this.desde}`)
                    .then(() => {
                        window.location.href = "/api/pdf"
                        this.num_cuenta = ""
                        this.desde = ""
                    })
            }
        },
    },
    computed: {


    },

}).mount("#app");