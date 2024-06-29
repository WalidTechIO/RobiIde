import GRect from "./GRect.jsx";
import GImage from "./GImage.jsx";
import GOval from "./GOval.jsx";
import GText from "./GText.jsx";

export default function ObjectRenderer({object}) {

    //World rendering
    if(object.type === "GWorld") {
        
        const style = {
            position: "relative",
            backgroundColor: object.color,
            width: `${object.dimension.width}px`,
            height: `${object.dimension.height}px`,
        }

        return <div style={style}>
            {object.childrens.map(children => {
                return (<ObjectRenderer key={children.uuid} object={children} />)
            })}
        </div>
    }

    //Common objects
    return <>
        {object.type === "GRect" && <GRect rectangle={object} />}
        {object.type === "GImage" && <GImage image={object} />}
        {object.type === "GOval" && <GOval oval={object} />}
        {object.type === "GText" && <GText text={object} />}
    </>

}