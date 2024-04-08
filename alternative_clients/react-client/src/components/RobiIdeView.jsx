import CodingView from "./CodingView.jsx";
import useRobiClient from '../hooks/useRobiClient.jsx';

export default function RobiIdeView() {

    const robiclient = useRobiClient()

    return <div className="container mt-5">
        <CodingView robiclient={robiclient}/>
        <hr />
        {robiclient.env}
        {robiclient.renderer}
    </div>
}